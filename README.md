
# Themes-tomee1.7.1-plus-deltaspike

A final working project generated with Primefaces Crud Generator.
 
Used: 
TomEE1.7.1-plus server.
Database  mysql named derby_sample_mod.

At generated web app with Primefaces Crud Generator i have added functionality to  select a Theme at runtime to change the look of Web app.

So i added at web.xml the following context-param:

<context-param>
        <param-name>primefaces.THEME</param-name>
        <param-value>#{userSettingsBean.theme.name}</param-value>
    </context-param>

into source java i added also a package named: management.themes that contain:

Theme.java
ThemeConverter.java
UserSettingsBean.java (SessionScoped). So the selected Theme  remains stored 
 for the whole session of the user.

Into webpages at dir web/resources i added a dir named images/themes that contain 
the thumbails of Themes.
So when the user try to change the Theme at runtime, it can select it with a image
 of resulting theme selected.

Into resources/css/pfcrud.css i  modified the following class css
into .column {
    font-weight: bold;
    background-color: menu; 
}

I commented the: backfrong-color: menu;
the class .column result:


.column {
    font-weight: bold;
 /*   background-color: menu; */ 
}

With this modify i resolved the problem of labels of field at left side of pages:
 Create.xhtml, Edit.xhtml, View.xhtml, where the names of the labels were not visible.


Important: for run the netbeans project, resolve the  dependencies: 
from project right click and select properties/lib Compile.

Into libs add the primefaces-6.1.jar and all-themes-1.0.10.jar 
(download from primefaces web site).

Into the project right click and select properties/lib Processor 
and select from TomEE/lib  the following jars:

openjpa-2.4.0-nonfinal-1598334.jar

javaee-api-6.0-6-tomcat.jar

For other version of Tomee the names of jars above  can change for version .


For the persistence, control into WEB-INF/resources.xml file
the configuration of datasource.
Change the values for db connection ,username and password like your db
derby_sample_mod created into MySQL .




