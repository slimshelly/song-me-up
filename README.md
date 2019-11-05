# Song-Me-Up 

© Shell Yang, Maddie Becker, Sam Oliphant, and Thomas Vandermillen 2018

## About

**Song-Me-Up** is a Web Application that allows users to create playable, collaborative, and ranked music playlists in real time. It was executed as a final project for `CS0320: Introduction to Software Engineering` at Brown University in `Spring 2018`, borne out of the co-founders' desires to make music-requesting at parties a more enjoyable experience. 

PS. The name Song-Me-Up is a pun on the Brown CS Department's TA ticketing system, [Sign-Me-Up](https://signmeup.cs.brown.edu/).

## Maintanence
Each project member has forked the repository to update the project as they wish. This repository is my attempt to improve the usability, efficiency, and scalability of this application. Feel free to leave a comment for feature requests!
The rough division of labor for legacy code is as follows, although the team worked together to design the code contracts for each class.
* jyang80 - queries & web playback
* tvanderm - ranking algorithm
* mbecker09 - front end, web design
* soliphant - bridging front end and back end, authorization token

## Building/Running
1) Clone the repository.
2) Navigate to the `/song-me-up` directory.
3) To build, run `mvn package`.
4) To run, run `./run --gui` to host the application on `localhost:4567`. You can host 2 instances of this application congruently by opening one tab in `incognito mode` and another tab in `non-incognito mode`. 

## Using the Interface
Please follow the guidance provided in the UI. There's an FAQs tab in the top right hand corner that may answer your questions.

Below is an archive of the project spec. 
```
# cs0320 Term Project 2018
🔥Sign-Me-Up/ Song-requester for parties 🔥

**Team Strengths and Weaknesses:** 
- Strengths: Systems programming, OOP, HTML, CSS, Humor
- Weaknesses: Javascript, using libraries, connecting backend to frontend efficiently

**Project Scope:**
1) What problems do these ideas attempt to solve?
2) How your project will solve them?
3) What critical features you will need to develop, why are those features are being included, and what will you expect to be the most challenging about each feature?

**Answers**

1) So often, DJs at a party aren’t aware of the music tastes of the crowd, and do a poor job at stirring excitement. It’s also terribly difficult to scream in the DJ’s ear and tell them what you want to hear next. 
2) With a music requester, attendees can “Sign-Me-Up” their songs. DJ’s can “claim ticket” or “delete ticket” if they support or feel repelled by a particular requester’s music taste. You also don’t want all those crew boys requesting “Mr. Brightside” every 5 minutes. However, to counteract those mean DJ’s, if enough people request the same song, it will have to be played.
3) Some critical features are...
  - Receiving and updating the song requests in real time, managing concurrency threads 
  - Joining the server of polling data from different devices
  - Location sharing (DID NOT IMPLEMENT)
  - Visualizing the music that is requested - maybe pulling information on the song and having an interactive interface for both the DJ and the requesters.

**Mockup & Rough Spec:**
Mockup- https://wireframepro.mockflow.com/view/D4ac32b3e48f2ea059a4072c1cc985cf8#/page/a069862d6fb54fc9bbf934ef759f1911
Spec- https://docs.google.com/presentation/d/1VUB4MGLDvUJm22FoK3_i4An-As9YGxBhaWuYWOicpQo/edit?usp=sharing
```


