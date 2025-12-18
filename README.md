# CS-360-Module-Eight-Journal

**App Requirements, Goals, and User Needs**

The objective of the Weight Tracking Application was to develop a functional and user-friendly mobile app that allows users to securely log in and record their daily weight entries. Users can also set a personal goal weight and optionally receive SMS notifications. The app is designed to meet the needs of individuals seeking a reliable and straightforward method for tracking weight changes over time, without unnecessary complexity. By focusing on essential features such as data persistence, goal tracking, and accurate feedback, the application promotes accountability, consistency, and long-term health awareness.

**Screens, Features, and User-Centered UI Design**

Several screens were required to support user needs while maintaining a user-centered design. These included a login and account creation screen, a dashboard displaying daily weight entries in a grid format, a screen for adding new weight data, a goal-setting screen, and an SMS notification permission screen. The UI design prioritized clarity and ease of use, employing simple layouts, readable text, and intuitive button placement. Each screen focuses on a single task, reducing user confusion and minimizing unnecessary navigation. This approach was effective as it enabled users to complete actions quickly and understand the app’s purpose without needing instructions.

**Coding Approach and Development Strategies**

The app was developed using a structured and incremental coding approach. Features were implemented one at a time, beginning with core functionality such as login validation and database setup, before progressing to additional features like goal tracking and SMS permissions. Utilizing helper classes, clear naming conventions, and in-line comments helped maintain organized and readable code. Breaking the app into smaller components facilitated easier debugging and allowed for changes without impacting unrelated features. These strategies can be applied in future projects to enhance maintainability, scalability, and collaboration.

**Testing and Code Validation**

Continuous testing was conducted using the Android Emulator to ensure the app functioned as intended. Each central feature was tested independently, including account creation, login validation, database CRUD operations, navigation between screens, and SMS permission handling. Testing is crucial, as it helps identify logical errors, crashes, and unexpected behaviors early in the development process. This thorough testing revealed issues such as incorrect data types and lifecycle-related bugs, which were corrected before final submission, resulting in a more stable application.

**Innovation and Problem Solving**

Throughout the design and development process, innovation was necessary to overcome challenges related to permissions handling and database interactions. Implementing optional SMS notifications required careful consideration of user consent and fallback behavior if permission was denied. Ensuring the app continued to function normally without SMS access necessitated meticulous planning and testing. Moreover, resolving build errors and managing project size constraints for GitHub submission required adapting the project structure and removing unnecessary generated files.

**Demonstrated Knowledge and Technical Strengths**

One area in which I excelled was implementing persistent data storage using SQLite. Designing the database schema, managing user-specific data, and supporting create, read, update, and delete (CRUD) operations demonstrated my understanding of mobile data persistence and app architecture. Successfully integrating database operations with the UI and maintaining user state across sessions highlighted my ability to apply both technical skills and user-centered design principles in a real-world mobile application.
