/*
 * Copyright 2011 Philippe Blanc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;
import com.sun.tools.javadoc.Main;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ExcludeDoclet extends Doclet
{
  public static void main(String[] args)
  {
    String name = ExcludeDoclet.class.getName();
    Main.execute(name, name, args);
  }

  public static boolean validOptions(String[][] options, DocErrorReporter reporter)
  {
    List<String[]> standardOptions = new ArrayList<String[]>();

    for (String[] option : options)
    {
      if (option[0].equals("-output") && option.length == 2)
      {

        String output = option[1];

        File test = new File(output);

        if (test.exists() && test.isFile())
        {
          System.out.println("The output file must be a directory");
          return false;
        }

        if (!test.exists())
        {
          if (!test.mkdirs())
          {
            System.out.println("Cannot create output: " + test.getAbsolutePath());
            return false;
          }
        }

        if (!output.endsWith("/"))
        {
          output += "/";
        }

        Standard.htmlDoclet.configuration.docFileDestDirName = test.getName();
        Standard.htmlDoclet.configuration.destDirName = output;

        continue;
      }

      if (option[0].equals("-docletpath") && option.length == 2)
      {
        standardOptions.add(new String[] { "-taglet", "ExcludeTaglet" });
        standardOptions.add(new String[] { "-tagletpath", option[1] });
      }

      standardOptions.add(option);
    }

    options = standardOptions.toArray(new String[][] {});

    if (Standard.htmlDoclet.configuration.destDirName.length() > 0)
    {
      return Standard.validOptions(options, reporter);
    }

    System.out.println("You must specifiy a valid output path (-output)");
    return false;
  }

  public static int optionLength(String option)
  {
    if (option.equals("-output"))
    {
      return 2;
    }

    return Standard.optionLength(option);
  }

  public static boolean start(RootDoc root)
  {
    System.out.println("Loading ExcludeDoclet...");
    return Standard.start((RootDoc) process(root, RootDoc.class));
  }

  private static boolean exclude(Doc doc)
  {
    if (doc instanceof ProgramElementDoc)
    {
      if (((ProgramElementDoc) doc).containingPackage().tags("exclude").length > 0)
      {
        return true;
      }
    }

    return doc.tags("exclude").length > 0;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private static Object process(Object obj, Class expect)
  {
    if (obj == null)
    {
      return null;
    }

    if (obj.getClass().getName().startsWith("com.sun."))
    {
      return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new ExcludeHandler(obj));
    }
    else if (obj instanceof Object[])
    {
      Object[] objects = (Object[]) obj;
      List list = new ArrayList(objects.length);

      for (Object entry : objects)
      {
        if ((entry instanceof Doc) && exclude((Doc) entry))
        {
          continue;
        }

        list.add(process(entry, expect.getComponentType()));
      }

      return list.toArray((Object[]) Array.newInstance(expect.getComponentType(), list.size()));
    }
    else
    {
      return obj;
    }
  }

  private static class ExcludeHandler implements InvocationHandler
  {
    private Object target;

    public ExcludeHandler(Object target)
    {
      this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
      if (args != null)
      {
        String methodName = method.getName();
        if (methodName.equals("compareTo") || methodName.equals("equals")
            || methodName.equals("overrides") || methodName.equals("subclassOf"))
        {
          args[0] = unwrap(args[0]);
        }
      }
      try
      {
        return process(method.invoke(target, args), method.getReturnType());
      }
      catch (InvocationTargetException e)
      {
        throw e.getTargetException();
      }
    }

    private Object unwrap(Object proxy)
    {
      if (proxy instanceof Proxy)
      {
        return ((ExcludeHandler) Proxy.getInvocationHandler(proxy)).target;
      }

      return proxy;
    }
  }
}