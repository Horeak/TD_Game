# TD_Game

TD_Game is a Java-based tower defense game developed as a learning project to explore game development concepts and object-oriented programming principles. The game relies on the **GameBase** library and uses **Slick2D** for graphical rendering and game mechanics.

## Table of Contents

- [About](#about)
- [Features](#features)
- [Dependencies](#dependencies)
- [Installation](#installation)
  - [Prerequisites](#prerequisites)
  - [Setup](#setup)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## About

This project serves as a practical exercise in Java programming, focusing on the development of a tower defense game. Players can place towers strategically to defend against waves of enemies. The game includes graphical elements rendered with **Slick2D** and game logic built using the **GameBase** framework.

## Features

- **Graphical User Interface**: Interactive GUI for placing towers and managing gameplay.
- **Tower Placement**: Allows players to strategically place towers on a game grid.
- **Enemy Waves**: Simulates waves of enemies following predefined paths.
- **Basic Combat Mechanics**: Towers attack enemies automatically within their range.

## Dependencies

- **[GameBase](https://github.com/Horeak/GameBase)**: Core functionality and shared components.
- **[Slick2D](http://slick.ninjacave.com/)**: A 2D game library for Java used for rendering and game mechanics.

## Installation

### Prerequisites

- **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed on your system.
- **Git**: Required to clone the repository.
- **Slick2D**: Download the library and set it up in your development environment.

### Setup

1. **Clone the Repositories**:

   Clone both **TD_Game** and **GameBase** repositories:

   ```bash
   git clone https://github.com/Horeak/TD_Game.git
   git clone https://github.com/Horeak/GameBase.git
   ```

2. **Set Up Slick2D**:

   Download the [Slick2D library](http://slick.ninjacave.com/) and include the JAR file in your project classpath.

3. **Compile the Code**:

   Navigate to the `src` directories of both **GameBase** and **TD_Game**, and compile all Java files:

   ```bash
   javac -cp "path_to_slick2d/slick.jar" GameBase/src/**/*.java
   javac -cp "path_to_slick2d/slick.jar:GameBase/src" TD_Game/src/**/*.java
   ```

4. **Run the Application**:

   Execute the game by running the main class:

   ```bash
   java -cp "path_to_slick2d/slick.jar:GameBase/src:TD_Game/src" Main.MainFile
   ```

## Usage

1. **Start the Game**:

   Run the application using the command above to launch the game window.

2. **Gameplay Instructions**:

   - **Placing Towers**: Use the GUI to select and place towers on the game grid.
   - **Starting Waves**: Initiate enemy waves and observe as towers engage incoming enemies.
   - **Objective**: Prevent enemies from reaching the end of their path by strategically placing towers.

## Contributing

As this repository is archived and read-only, contributions are no longer accepted. However, feel free to fork the repository for personal exploration and learning.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.
