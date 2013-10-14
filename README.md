# Hokie Manager

Hokie Manager Authenticates with CAS and allows for access to Scholar to fetch upcoming events.

**Created By:** Nayana Teja Chiluvuri (tejachil@vt.edu)

**Source:** https://github.com/tejachil/HokieManager/tree/master/HokieManager/

**License:** GNU General Purpose License Version 2 (LICENSE.txt)

![alt tag](https://raw.github.com/tejachil/HokieManager/master/app_screenshot.png)

## Purpose
The purpose of this app is to integrate Virginia Tech's Scholar service on a mobile Android platform. The app is able to authenticate with the Central Authentication Service (CAS) of Virginia Tech and then proceed to access the Scholar service. It is currently able to login to the scholar website and receive the upcoming events from the calendar widget on scholar. After parsing the retrieved events, the app organizes the events into three categories: Classes, Assignments, and Office Hours. These three categories are displayed to the user in a clean and organized layout.

### Current Features
The app is still in its initial stages of development so the current features of the app are limited. The following features have been implemented:
* Authentication with the CAS
* Authentication with Scholar and able to login and progromatically access content within scholar
* Dynamic UI that allows for all network and time intensive tasks to run in the background
* Implementation of refreshing of the session in the case of a session timeout
* Login and logout functionality implemented in the UI
* Upcoming tasks for the next week are retrieved, categorized, and displayed in an organized fashion upon request (press of the refresh button on the action bar)
* Scrollable Tabs UI implemented with fragments for the Scholar Activity to allow for easy expansion of the project
* Action Bar implemented in the Scholar Activity to conform with the UI design etiquette of the latest Android API (Jellybean)

### Future Vision
I hope to add much more functionality to the app in the near future. The features that will be implemented in the next stages of the app development include the following:
* Option to enable automatic silence of cellphone during class and lecture
* Integration with a task or calendar service that automatically sends newly retrieved assignments to the app on the phone that manages tasks and events
* Implementation of periodic network polling for newly added tasks (once a day)
* Expansion to other scholar services such as resources and gradebook
* Use of CAS authentication methods to integrate Hokie SPA in a mobile platform

# High-Level Code Organization
Code is organized in an effort to maximize modularity and scalability. Each of the services of the app are devided amongs a class that implements the services' methods, an AsyncTask to run the methods of the class in a background thread, and an activity to interface with the UI layout and display the service.
* CAS Service
	- CentralAuthenticationService: implements the methods to login, logout, and maintain session cookies for CAS
	- AuthenticateTask: AsyncTask that runs the background network services to login to the CAS
	- LoginActivity: Android activity that interacts with the UI and calls the AsyncTask
	- activity_login: layout for the login activity
* Scholar Service
	- Scholar: implements the methods to login to Scholar, maintain session cookies for Scholar, and parse the events
	- ScholarTask: AsyncTask that runs the scholar network services in the background
	- ScholarActivity: Android activity that displays the fragmented scrollable tabs UI for the scholar layout
	- activity_scholar: layout for the scholar activity
Data is passed between the activities using bundles. A class called GlobalApplication that extends Application was created with public member variables that can be accessed by multiple activities by invoking the context of the the application through getApplication()
The CAS methods and services are independent of the others. The Scholar service requires the CAS service to authenticate. The application can easily be scaled with the current strucure of the software.

### UML Class Diagram
![alt tag](https://raw.github.com/tejachil/HokieManager/master/ClassDiagram.png)

### Sequence Diagrams
![alt tag](https://raw.github.com/tejachil/HokieManager/master/cas_sequence.png)
_____________________________________________________________________________
![alt tag](https://raw.github.com/tejachil/HokieManager/master/scholar_sequence.png)
