[![Build Status](https://travis-ci.org/dhowe/RiTa.svg?branch=master)](https://travis-ci.org/dhowe/RiTa) <a href="http://www.gnu.org/licenses/gpl-3.0.en.html"><img src="https://img.shields.io/badge/license-GPL-orange.svg" alt="npm version"></a> <a href="https://www.npmjs.com/package/rita"><img src="https://img.shields.io/npm/v/rita.svg" alt="npm version"></a>

## RiTa: the generative language toolkit

### Installation in Android
--------
#### Eclipse with ADT Plugin

1. In Eclipse select 'File' > 'New' > 'Project...' > 'Android' > 'Android Application Project'

2. Name Application Name 'Rita Example' and select 'API 15' as the Minimum Required SDK

3. Keep pressing 'Next' and select 'Blank Activity' when prompted and finish

4. Edit 'activity_main.xml' and select 'activity_main.xml' at the bottom (next to 'Graphical Layout' tab)

5. Replace it with the following code:
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
6. Download and drag [RiTa.jar](http://rednoise.org/rita/download/rita.jar) into the libs folder of RitaExample project

7. When prompted select 'Copy File' then right click on it select 'Build Path' > 'Add to Build Path'

8. To test a simple example, replace the code of 'MainActivity.java' with the following:
	```java
	package com.example.ritaexample;

	import android.app.Activity;
	import android.os.Bundle;
	import android.widget.TextView;

	import rita.*;

	public class MainActivity extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

	        String [] a = RiTa.tokenize("The cat ate the stinky cheese.");

	        TextView t = (TextView) findViewById(R.id.textView);

	        t.setText( java.util.Arrays.toString(a) );
		}
	}
	```
9. Right click on RitaExample and select 'Run As' > 'Android Application'

&nbsp;

#### With Android Studio

To install:

1. Start a new Android Studio project with application name 'Rita Example' and company domain 'example.com', select 'Blank Activity' and then finish

2. In the project window 'manifests', 'java' and 'res' packges can be seen under 'app'

3. Right click on 'java' and create a new package called 'libs', dragging the [rita.jar](http://rednoise.org/rita/download/rita.jar) inside

4. When prompted check 'Search for references' and select 'Unlock files'

5. Right-click on the jar file > 'Add As Library...'

6. Add 'import rita.*;' to the top of your .java file

7. To test a simple example, replace the code of 'MainActivity.java' with the following:
	```java
	package com.example.ritaexample;

	import android.app.Activity;
	import android.os.Bundle;
	import android.widget.TextView;

	import rita.*;

	public class MainActivity extends Activity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);

	        String [] a = RiTa.tokenize("The cat ate the stinky cheese.");

	        TextView t = (TextView) findViewById(R.id.textView);

	        t.setText( java.util.Arrays.toString(a) );
		}
	}
	```


#### Can I contribute?
--------
Please! We are looking for more coders to help out... Just press *Fork* at the top of this github page and get started, or follow the 'Development Setup' instructions at the bottom of [this page](https://github.com/dhowe/RiTa)...
