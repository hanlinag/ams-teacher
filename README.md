# ams-teacher
Android app for teacher for Attendance Management System project. Teacher app has the full privileges over the system such as student management, attendance management, etc.

### Get Student App Here
Link: [Student App Repository](https://github.com/hanlinag/ams-student) 

# Introduction 
This project is “Attendance Management System”. This system is provided for  three main parts (the dean, the teacher and the student) to do taking attendance, calculating the percentage of the attendance and receiving the medical leaves. In the dean panel, the dean can accept the student attendance record that the class teacher sent. The dean can also accept the medical leaves and the system can update the corresponding student attendance automatically. She can also inform the underrated students and other important information. In the teacher panel, the teacher can generate QR code for the appropriate class time and he/she can confirm the attendance from the student and then submit to the dean. In the student panel, the student can scan the generated QR code from the teacher to get their attendance. The student can also view their attendance percentage transparently and can take the medical leaves by uploading the medical letter and choosing the dates that they were absent. 

This system supports simplicity, time saving and accuracy. Thus, the attendance management system can be claimed as a tool for universities or offices as different features of our system can aid the users in different ways.

## Teacher app
For the lecturers/teachers, the system also provides android application for them, but the difference here is that they have the authority to generate QR codes for students to take their attendance and teachers have responsibility to submit the roll call report to the dean after confirming that the attendance data is right.

For the dean, the system also provides for the dean panel. He/she can notify the underrated attendance records, accept medical leaves by checking whether the medical leave letters are eligible or not. He/she can also manage all information, such as subjects and its information, students’ information, and teachers’ information. The dean can view each student attendance record daily, monthly or by semester.

Dean has full access to control everything including teachers and students management.

## Functions 
### Teacher Panel
- Login (Enter teacher-ID and password)
- Generate QR code for attendance
- Check attendance information
- Submit attendance data to dean
- Logout

### Dean Panel
- Login (Enter user-ID and password)
- Check the medical leaves and accept
- Manage information about students, teachers and subjects
- Notify the underrated attendance students and some information
- View the attendances of each students monthly or by semester

## How we handle validation?
What if the student is at his hostel and wanna cheat on his attendance?
Well, we also considered for that case too. Here's how we did:
- The application asks for the location of the student's phone. If his/her location is not in the range(10 m) of teacher app, it cannot scan the QR code. 
- QRCode is encrypted. So, it cannot be scanned using third party apps. You will not get the information on the QRCode.
- QRCode will be expired in 5 minutes. 
- Attendance only counts if the subject teacher press the CONFIRM button. 

To avoid the wrong time and classroom, 
- We also validate the timetable for the teacher to collect attendance.
- If it's not on the data and time of the particular subject (For example, Monday from 9 - 10,). 
- If you're on Tuesday or at 10:30 AM, you cannot collect the attendance (system will not allow you to generate the attendance QRCode).
- After students scan the QRCode teacher provided, the teacher can know ubsent students, current students in class, and toal students registered in the current class.
- By the time teacher press CONFIRM button, the attendance records will be stored in the database. 


## Will it work for me?
This project is not designed for general usecase. It is designed based on the manual system of [University of Computer Studies, Mandalay](https://www.ucsm.edu.mm). Author is a computer science student who's studying in there majoring in Knowledge Engineering. But you can change some code it work with yours. Hope this can help you in some way, as the reference.

## Project Setup
- I used firebase for realtime database.
- You'll need to add the dependencies according to the [Firebase documentation](https://firebase.google.com/docs/database/android/start?authuser=0)
- Note that you need to create the project in Firebase in order to access the realtime database from there. 
- Then link your firebse project to this source code in [Android Studio](https://developer.android.com/studio)
- After it finishes building the project, you're good to go.

## Technologies Used
1. Kotlin 
2. Google Firebase for data storing and realtime database
3. JSON

## Software Specification 
- Android 4.4 Kit Kat minimum
- Google GPS service

# System Design 
### Teacher app flowchart
Teacher needs to login firstly by using his/her teacher-ID and password. When he can login correctly, he can generate the QR code to take attendance for his class. He receives the attendance records from the students and can check whether it is right. Then he can submit the attendance data to the dean.
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/teacherflow.png?raw=true)

Dean can firstly login with her id and password. Then she can do every function in the dean panel. She can manage the information about subjects, students and teachers. She can check and notify the underrated attendance students and some information. She can also check the uploaded medical leaves from the students and then accept them. Then the system will automatically refill for their absent attendance. She can generate the student attendance record with the CSV file monthly or for each term.
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/deanflow.png?raw=true)

### Sequence Diagram
The processes of the system describe that the teacher generates the QR code to take the attendance and the students scan this QR code to get attendance. The teacher checks the attendance data and sends to the dean by submitting the data. The student can view the attendance record and upload the medical leaves. Then the dean accepts the medical leaves and the attendance percentage will be updated. The dean manages the subjects and generates records. 
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/sequence.png?raw=true)

### Usecase Diagram
Student can update his/her information and password, view his/her attendance percentage, upload medical leave letters and scan QR code to get attendance. The teachers can generate QR code for the students to take the attendance and then check the attendance information and submit the attendance record to dean. The dean can notify underrated attendance students, accept medical leave letters and update corresponding student attendance, manage all information about the subjects, students and teachers. The dean can also generate student attendance record with CSV file.
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/usecase.png?raw=true)

### Database Design
Overall Design
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/overalldb.png?raw=true)

Subject Table
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/subject.png?raw=true)

Teacher Table
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/teacher.png?raw=true)

Student Table
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/student.png?raw=true)

Pre-Student Table (who haven't registered yet.)
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/pre-students.png?raw=true)

Pending Attendance Table (Wait for the subject teacher to confirm the attendance.)
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/pending%20attendance.png?raw=true)

Medical Leave Table
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/medical%20leave.png?raw=true)

Class Table
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/class.png?raw=true)

Attendance Record Table
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/addtendance.png?raw=true)

# Encryption Algorithm
This system is used [AES](https://en.wikipedia.org/wiki/Advanced_Encryption_Standard) (Advanced Encryption Standard) algorithms. The features of AES are as follows −
- Symmetric key symmetric block cipher
- 128-bit data, 128/192/256-bit keys
- Stronger and faster than Triple-DES
- Provide full specification and design details
- Software implementable in C and Java


We encrypt user passwords, QR code data, etc.

# Screenshots
## Teacher Panel
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/ss1.png?raw=true)
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/ss2.png?raw=true)

## Dean Panel
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/ss3.png?raw=true)
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/ss4.png?raw=true)
![alt text](https://github.com/hanlinag/ams-teacher/blob/master/images/ss5.png?raw=true)


# Conclusion 
This attendance management system is developed for the dean, the teacher and the student. By using this system, the dean can reduce his/her daily or monthly complex jobs. He/She can easily check the underrated attendance students and then inform them. It is not necessary to manually calculate for monthly attendance. He/She does not need to do any complex task for medical leaves. For the teacher, the teacher does not need to get the attendance record paper from the student affairs and return it. It takes less times than manual system, so the teacher can teach her lesson longer.  For the student, the student can view their attendance transparently and he/she notices that if his/her attendance is underrated. He/she can also view the attended dates on the specific subject.

# License
[MIT License](LICENSE)
