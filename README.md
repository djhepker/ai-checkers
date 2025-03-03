AI Checkers Game
This project is a console-based implementation of the classic game of checkers featuring an AI opponent. The AI leverages a custom Q-learning algorithm, a form of reinforcement learning, to make decisions and enhance its gameplay over time. SQLite is used to manage game data, such as storing Q-values or game states, ensuring efficient data handling.
Features
• Play checkers against an AI opponent in a JFrame interface

• Custom Q-learning algorithm for intelligent AI decision-making

• SQLite database for robust and efficient data management

• Built with Java and Maven for streamlined development and deployment

Installation Instructions
To set up the project on your local machine, follow these steps:
Clone the repository:
bash

git clone https://github.com/yourusername/ai-checkers.git

Replace yourusername with your actual GitHub username or organization name.

Navigate to the project directory:
bash

cd ai-checkers

Build the project using Maven:
bash

mvn clean install

Run the application:
bash

java -jar target/ai-checkers-1.0-SNAPSHOT.jar

Usage
Once the application is launched, you will select the type of game as well as the piece color of your choosing. The AI will make moves based on its Q-learning algorithm, or stochatically if you wish for a completely random opponent. Play multiple games to see the AI adapt and improve its strategy over time, or enable training mode in Main.

Dependencies
The project relies on the following dependencies, all managed via Maven:
Lombok (v1.18.36): Reduces boilerplate code with annotations.

HikariCP (v5.1.0): Provides efficient database connection pooling (excludes SLF4J to avoid conflicts).

Logback (v1.5.3): Handles logging for the application.

SQLite JDBC (v3.49.1.0): Enables interaction with the SQLite database.

Maven Dependency Analyzer (v1.15.1): Analyzes project dependencies during the build process.

No manual installation of these dependencies is required, as Maven handles them automatically.

Build Configuration
The project is configured to use Java 23 and requires Maven 3.8 or higher. The POM includes the following plugins for build management:
Maven Compiler Plugin (v3.11.0): Compiles Java code and processes Lombok annotations.

Maven Checkstyle Plugin (v3.6.0): Enforces coding style guidelines, configured via src/main/resources/checkstyle.xml.

Maven Jar Plugin (v3.3.0): Creates an executable JAR with the main class set to hepker.Main.

Maven Enforcer Plugin (v3.5.0): Ensures Java version compatibility (21-23), Maven version (3.8+), and dependency convergence.

Contributing
Contributions are welcome! To contribute to the project, please follow these steps:
Fork the repository.

1. Create a new branch for your feature or bug fix:
bash

git checkout -b feature/your-feature-name

2. Make your changes and commit them with clear, descriptive messages.

3. Push your changes to your fork:
bash

git push origin feature/your-feature-name

4. Submit a pull request to the main repository.

License
This project is licensed under the MIT License. See the LICENSE file for details.



