# SENG-300-Iteration 2

## Requirements:

1. Java 17 JDK
2. JUnit 4

### Recommended IDE:

1. Eclipse 2021-12

## Understanding the file structure

* The repository contains 3 main folders (these are 3 Eclipse projects):
    + SCS - Hardware - v2.0
        + This is a project that contains a simulation of hardware for a self checkout station. Written by Dr. Walker.
    + SCS - Software
        + This project contains all the software classes that interacts with the hardware interface.
        + The class that ties most of the software together is the class named "SelfCheckoutStationSoftware.java"
        + Package description:
            + The `bank` package contains a mock bank that allows us to create accounts, cards, store data and bill 
        the customer.
            + The `checkout` package contains anything that is related to the checkout process.
            + The `interrupt` package contains code that handles the event notifications from the hardware. 
            The hardware sends an event that "interrupt" the software and modifies it's state.
            + The `store` package contains classes that relate to a store
            + The `user` package contains classes that mimicks a user of the system.
    + SCS - Software - Test
        + This project contains all the JUnit 4 tests for our software.

## Downloading the project:

* Easiest way is to clone the repository:
    + [SENG-300-Iteration2 GitHub](https://github.com/JPlosz/SENG-300-Group-9)
        + You will need to be added to the repository as it is a private repository.
    + Alternatively, if you're a TA or Professor, you need to move our code into a file system where you have
    permissions to run and compile code.

## Compiling the code

1. Using Eclipse:
    1. File
    2. Import
    3. Existing Projects into Workspace (in the general dropdown)
    4. Click browse and select the folder that *contains the 3 projects Eclipse*
    5. You should see the 3 projects listed above, select all 3 projects.
    6. Click finish.
        * In your project explorer, you should see all 3 projects that we listed above.
    7. Expand SCS - Software - Test -> src -> org.lsmr.selfcheckout.software.test
        * Inside this package you have all the tests and running code.
        * Choose your class you would like to run and click the run button on the top tool bar of eclipse.
            + Note: You will need JUnit 4 installed and you may need to fix your project setup so that it includes JUnit 4.

## Contributors:

* Tyler Chen (30066806)
* Joshua Plosz (30084104)
* Michelle Cheung (30116197)
* Yunfan Yang (30067857)
* Sharjeel Junaid (30008424)
* Thuy Dinh (30031949)
* Rayner Nyud (30120995)
* Abdelhak Khalfallah (30081858)
* Khaled Mograbee (30095387)
