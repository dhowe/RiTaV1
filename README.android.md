[![Build Status](https://travis-ci.org/dhowe/RiTa.svg?branch=master)](https://travis-ci.org/dhowe/RiTa)

## RiTa: the generative language toolkit (for android)

<a href="http://rednoise.org/rita"><img height=80 src="http://rednoise.org/rita/img/RiTa-logo3.png"/></a>

### [The RiTa website](http://rednoise.org/rita)

RiTa is designed to be an easy-to-use toolkit for experiments in natural language and generative literature. It is implemented in Java and JavaScript (with a unified API for bothl). It is free/libre and open-source according to the GPL license.

About the project
--------
* Author:   [Daniel C. Howe](http://rednoise.org/daniel)
* License:  GPL (see included [LICENSE](https://github.com/dhowe/RiTa/blob/master/LICENSE) file)
* Web Site:          [https://rednoise.org/rita](http://rednoise.org/rita)
* Github Repo:       [https://github.com/dhowe/RiTa](https://github.com/dhowe/RiTa)
* Bug Tracker:       [https://github.com/dhowe/RiTa/issues](https://github.com/dhowe/RiTa/issues)
* Reference:    [https://rednoise.org/rita/reference](http://rednoise.org/rita/reference))

In android
--------
#### Eclipse with ADT Plugin

To install:

1. Download and drag [RiTa.jar](http://rednoise.org/rita/download/rita-latest.jar) into the libs folder of an existing project
2. When prompted select 'Copy File' then right click on it select 'Build Path' > 'Add to Build Path'
3. include 'import rita.RiTa;' at the head session of the java file

To run:

1. In Eclipse select 'File' > 'New' > 'Project...' > 'Android' > 'Android Application Project'
2. Name Application Name 'Rita Example' and select 'API 15' as the Minimum Required SDK
3. Keep pressing 'Next' and select 'Blank Activity' when prompted and finish
4. Edit 'activity_main.xml' and select its 'activity_main.xml' at the bottom of the window (next to 'Graphical Layout' tab)
5. Replace it with the following code
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.example.ritaexample.MainActivity" >

    <TextView android:text="@string/hello_world" android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/textView"
        android:layout_centerVertical="true"
        android:layout_centerHorizontal="true" />

    <RelativeLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@+id/textView"
        android:layout_marginLeft="94dp"
        android:layout_marginTop="86dp"
        android:layout_toRightOf="@+id/textView" >
    </RelativeLayout>

</RelativeLayout>
```
6.add [RiTa.jar](http://rednoise.org/rita/download/rita-latest.jar) into the libs folder of RitaExample project

7.When prompted select 'Copy File' then right click on it select 'Build Path' > 'Add to Build Path'

8.replace the code of 'MainActivity.java' with the following
```java
package com.example.ritaexample;

import java.util.Arrays;

import rita.RiTa;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        String [] a = RiTa.tokenize("The cat ate the stinky cheese.");

        TextView t = (TextView) findViewById(R.id.textView);

        t.setText( Arrays.toString(a) );
	}
}
```
9.Right click on RitaExample > 'Run As' > 'Android Application'

<h5>Android Studio</h5>

To install:

1. 'Start a new Android Studio project' with Application name 'Rita Example' and Company Domain 'example.com' and select 'Blank Activity' and then finsih
2. in the project window (alt + 1 / cmd + 1) 'manifests', 'java' and 'res' packges can be seen under 'app'
3. right click on 'java' and create a new 'Package' called 'libs', drag the [rita-latest.jar](http://rednoise.org/rita/download/rita-latest.jar) inside
4. when prompted check 'Search for references' and select 'Unlock files'
5. right click on the jar file > 'Add As Library...'
6. Add 'import rita.*;' to the java file 

To run:

copy and paste the following code to 'MainActivity.java'
```java
package com.example.ritaexample;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

import rita.*;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String [] a = RiTa.tokenize("The cat ate the stinky cheese.");

        TextView t = (TextView) findViewById(R.id.textView);

        t.setText( Arrays.toString(a) );
    }
}
```

#### Can I contribute?
--------
Please! We are looking for more coders to help out... Just press *Fork* at the top of this github page and get started, or follow the instructions below... 

#### Development Setup
--------
- Download and install [npm](https://www.npmjs.org/). The easiest way to do this is to just install [node](http://nodejs.org/). 

- [Fork and clone](https://help.github.com/articles/fork-a-repo) this library. 

    - First, login to github and fork the project

    - Then, from a terminal/shell: 
  ```bash
  $ git clone https://github.com/dhowe/RiTa.git
  ```

...

- Work on an existing [issue](https://github.com/dhowe/RiTa/issues?q=is%3Aopen+is%3Aissue+label%3Aandroid), then [submit a pull request...](https://help.github.com/articles/creating-a-pull-request)
