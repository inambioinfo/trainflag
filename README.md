_Whilst TrainFlag hasn't had a full release yet, it is now functional, so if you want to compile the sources and have a play, feel free_

# TrainFlag
TrainFlag is a flagging and survey application designed for use in compter based training environments.  It provides a way for the trainer to be able to monitor the progress of a large number of students, and be able to see who requires help.

![TrainFlag Client](/images/trainflag_client.png)

The application comes in two parts, a server run by the trainer, and a client which is run by all of the students.  The client allows the students to let the instructor know how they're getting on, and to ask for help.

![TrainFlag Server](/images/trainflag_server.png)
![TrainFlag Server](/images/room_view.png)

From the server, the instructor can see the status of all of the students.  They can also configure a number of multi-choice surveys which they can launch at any time.  These will be displayed on the client machines, from where answers can be collated , and results obtained to allow for more interactive training.

![Question Editor](/images/question_editor.png)

## Implementation
TrainFlag is written in java and is therefore platform independent and will work on any system which has a java runtime environment installed.  It ideally requires that the server and clients are on the same network subnet (it can be used across subnets, but auto-discovery won't work).  The application doesn't require administrative permissions to run, and needs no additional infrastructure beyond the application distributed here.

## Suggestions
If you have any suggestions for the development of TrainFlag, please send these to simon.andrews@babraham.ac.uk.
