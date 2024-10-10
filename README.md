# Optimized Fire-fighters Scheduler

## Project Overview

The Optimized Fireman Scheduler is designed to optimize the allocation of firefighting resources in response to ongoing fires within a specified area. This project implements a dynamic scheduling system that ensures efficient and effective deployment of fireman groups based on various critical factors.

## Key Features

### 1. Nearest Path Calculation

- **Algorithm:** Dijkstra's Algorithm and Dynamic programming
- **Description:** The scheduler calculates the shortest path to each fire, ensuring that fireman groups can reach the fire sites as quickly as possible.

### 2. Resource Allocation

- **Description:** The system checks if each fireman group has sufficient resources to extinguish the fire. This includes evaluating the availability of water, equipment, and personnel.

### 3. Fire Danger Assessment

- **Description:** The scheduler assesses the danger level of each fire, prioritizing more dangerous fires to ensure that the most critical situations are addressed first.

## Project Goals

The primary goal of the Intelligent Fireman Scheduler is to optimize the process of allocating firefighting resources dynamically. By considering factors such as proximity, resource availability, and fire danger, the system aims to improve response times and effectiveness in managing fire emergencies.

## Implementation Details

### Dynamic Scheduling

- **Approach:** The scheduler dynamically adjusts the allocation of resources based on real-time data about ongoing fires and available resources.
- **Optimization:** The system continuously updates its calculations to ensure optimal deployment of fireman groups.

### Algorithms and Techniques

- **Dijkstra's Algorithm:** Used for calculating the shortest paths to fire locations.
- **Dynamic Programming:** Implemented to provide an alternative method for shortest path calculation, enhancing the robustness of the scheduling system.
- **Resource Check:** Ensures that each fireman group has the necessary resources to handle the fire.
- **Danger Assessment:** Prioritizes fires based on their danger levels to allocate resources effectively.

### Comparison of Algorithms
#### Dijkstra’s Algorithm vs. Dynamic Programming:
- **Efficiency**: Both algorithms were evaluated for their efficiency in calculating the shortest paths. Dijkstra’s algorithm is known for its efficiency in graphs with - non-negative weights, while dynamic programming offers a flexible approach that can handle a variety of scenarios.
- **Performance**: The performance of both algorithms was compared in terms of speed and accuracy. The results showed that while Dijkstra’s algorithm is faster in certain cases, the dynamic programming approach provides more flexibility and can be more effective in complex scenarios.

## Testing Instructions

To test the project, follow these steps:

1. **Navigate to the project directory:**

    ```sh
    cd path/to/project
    ```

2. **Compile the TestSimulation.java:**

    ```sh
    make Simulation
    ```

3. **Run the simulation on different maps:**

    ```sh
    make exeSimulation carteSujet
    make exeSimulation desertOfDeath
    make exeSimulation mushroomOfHell
    make exeSimulation spiralOfMadness
    ```

## Project Structure

- **src:** Contains the classes provided by the instructors.
  - **LecteurDonnees.java:** Reads all elements from a data description file (cells, fires, and robots) and displays them. You need to MODIFY this class (or write a new one) to create objects corresponding to your own classes.
  - **TestLecteurDonnees.java:** Reads a data file and displays its content.
  - **TestInvader:** Creates a "mini Invaders" simulator in a graphical window.

- **cartes:** Contains some example data files.

- **bin/gui.jar:** Java archive containing the graphical interface classes. See an example of its usage in TestInvader.java.

- **doc:** Documentation (API) for the graphical interface classes contained in gui.jar. Entry point: index.html.

- **Makefile:** Provides explanations on command-line compilation, including the concept of classpath and the use of gui.jar.

---

Feel free to let me know if there's anything else you'd like to add or modify!