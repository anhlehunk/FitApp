# Fitsapp
## Overview
Fitsapp is a fitness app that users can use to start working out. This app consists of multiple activities the user can choose to do, 
whereas most(free) fitness app on the app store only consists of one component. The app consists of 4 activities: a step counter, an exercise database, an achievements page, and a list consisting all the exercises the user chose to save.

## Technical Design
### Login
The first time a new user opens the app, a sign in page will appear where the user can login with their google account or with e-mail.
The app communicates with FireBase and Google to login of sign up.
On signing in for the first time, an user creates an unique ID which is created in FireBase: the online database this app uses to store data. The user will be headed to the main activity or homescreen when logged in. Users don't have to login again the next time, if the user restarts the app. They will see the homescreen unless the user is not logged in. 

### Activities
In the homescreen, the user can head to four activities, or log out. The first activity in the app is a step counter.
The step counter uses a sensor in android devices (step count sensor) and shows the user how many steps the user has taken since the last reset. The stepcount activity saves two digits: a step count since reset, which shows the current progress and a the total amount of steps the user has taken since the first signup. The step count since reset is stored locally and the total amount of steps is stored online.
The step count since reset is only stored locally, because it's only task is to show the user how much the user has progressed since resetting the step counter. The total amount of steps is saved online, because this data is needed for another activity: the achievements activity. The user can set a certain amounts of step he/she wants to take. This will be a personal goal.  
Depending on the set goal, a circular progress bar will be shown with a percentage, where the progress will be shown, if the users are coming closer to their goal.
This activity also consists of two buttons: one for resetting the current amount of steps taken and another one to enter the user's goal.

The second activity is the exercises database. This activity uses the API of wger.de, which is an online database consisting of muscle groups, exercises etc. 
On starting the activity, the user will see a spinner listview, and an image of a man. The image of a man is an image where a certain muscle group is highlighted, depending on the selected muscle group in the spinner. The user can select one of the 15 muscle groups in the list, and the image of the man changes accordingly, so that the user knows which group of muscles they are targeting in their body when selecting a muscle group in the list. 
Depending on the choice, the app searches for exercises that targets the selected muscle group. The user will see what they have selected in the title (the muscle group in latin), and a list consisting of exercises. On clicking on of the items in the list, the user will be headed to another activity, which show the description of the exercise and the images, showing how it should be done. On this activity, there is a save button where the user can save the current exercise. This data will also be saved in FireBase. The name of the exercise is the key, and the value will be the ID of the exercise.
The name of the exercise is needed to fetch the description from the API and the ID of the exercises is needed to fetch the images from the database. The images are not included in the same path as the exercises information, which was actually pretty hard to deal with, but this will be discussed later on in this report.

The third activity is a simple listview consisting of all the saved exercises. Clicking an exercise will show the exercise description and image, and long-clicking an item will cause the exercise to be deleted from the list, and in FireBase.

The fourth activity an achievements activity, where the user can keep track of his progress since using the app. This activity is used to motivate the user. The app consists multiple progress bars, which will slowly fill as the user completes certain tasks, like: have a total step count of 1000, or 5000 etc.

### Navigation
Each activity consist of a action bar, where the user can head to every main activity in this app.
The navigation consists of icons. This choice will be discussed later on.

## In Detail

## Challenges

## Decisions



![](plaatje.png)
