/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package com.begrud.beatemup;

public final class R {
    public static final class attr {
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int metaButtonBarButtonStyle=0x7f010001;
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int metaButtonBarStyle=0x7f010000;
    }
    public static final class color {
        public static final int black_overlay=0x7f040000;
    }
    public static final class drawable {
        public static final int buttondelete=0x7f020000;
        public static final int buttondeleteall=0x7f020001;
        public static final int buttondown=0x7f020002;
        public static final int buttonnormal=0x7f020003;
        public static final int buttonplay=0x7f020004;
        public static final int buttonportal=0x7f020005;
        public static final int buttonspeaker=0x7f020006;
        public static final int buttonstop=0x7f020007;
        public static final int buttonup=0x7f020008;
        public static final int ic_launcher=0x7f020009;
        public static final int particle=0x7f02000a;
        public static final int ringblue=0x7f02000b;
        public static final int ringorange=0x7f02000c;
        public static final int ringred=0x7f02000d;
        public static final int ringreddown=0x7f02000e;
        public static final int ringredup=0x7f02000f;
        public static final int tiledarkblue=0x7f020010;
        public static final int tilegreen=0x7f020011;
        public static final int tilepurple=0x7f020012;
        public static final int tilered=0x7f020013;
        public static final int tilewhite=0x7f020014;
        public static final int tileyellow=0x7f020015;
        public static final int win=0x7f020016;
    }
    public static final class raw {
        public static final int clap1=0x7f030000;
        public static final int snare1=0x7f030001;
    }
    public static final class string {
        public static final int app_name=0x7f050000;
        public static final int dummy_button=0x7f050001;
        public static final int dummy_content=0x7f050002;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.
    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.
        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.
    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f060000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f060001;
        public static final int ButtonBar=0x7f060003;
        public static final int ButtonBarButton=0x7f060004;
        public static final int FullscreenActionBarStyle=0x7f060005;
        public static final int FullscreenTheme=0x7f060002;
    }
    public static final class styleable {
        /** 
         Declare custom theme attributes that allow changing which styles are
         used for button bars depending on the API level.
         ?android:attr/buttonBarStyle is new as of API 11 so this is
         necessary to support previous API levels.
    
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_metaButtonBarButtonStyle com.begrud.beatemup:metaButtonBarButtonStyle}</code></td><td></td></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_metaButtonBarStyle com.begrud.beatemup:metaButtonBarStyle}</code></td><td></td></tr>
           </table>
           @see #ButtonBarContainerTheme_metaButtonBarButtonStyle
           @see #ButtonBarContainerTheme_metaButtonBarStyle
         */
        public static final int[] ButtonBarContainerTheme = {
            0x7f010000, 0x7f010001
        };
        /**
          <p>This symbol is the offset where the {@link com.begrud.beatemup.R.attr#metaButtonBarButtonStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name com.begrud.beatemup:metaButtonBarButtonStyle
        */
        public static final int ButtonBarContainerTheme_metaButtonBarButtonStyle = 1;
        /**
          <p>This symbol is the offset where the {@link com.begrud.beatemup.R.attr#metaButtonBarStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name com.begrud.beatemup:metaButtonBarStyle
        */
        public static final int ButtonBarContainerTheme_metaButtonBarStyle = 0;
    };
}