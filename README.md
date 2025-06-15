# Maze of Magic
Maze of Magic is a 2D top-down action game built with **LibGDX**. The game combines exploration, combat, and a mini-game challenge.    Players navigate a mysterious maze, avoid ghost enemies, shoot magic stars, and reach the glowing magic circle to enter a puzzle-based mini-game. The game features two distinct gameplay styles and is designed for casual gamers who enjoy fast-paced reflexes and light puzzle-solving.

## Team Members and Contributions
### Zhuoliang Xie
- Designed and implemented:
- MiniGameScreen.java
- GameOverScreen.java with Restart and Exit buttons
- Win logic and animation screen
- Sound effects: win, lose, and shooting
- Basic UI integration and asset setup

### Jiayi Hou
- Designed and implemented:
- GameScreen.java: player movement, camera follow, star shooting
- Enemy.java: enemy AI, collision logic
- MainMenuScreen.java: background, title, Start and Exit buttons
- Collision detection, win/lose conditions, game loop logic
- Basic UI integration and asset setup

## Game Features
- Free movement system with camera following the player
- Enemies spawn from all sides and chase the player
- Players can shoot projectiles (stars) by clicking
- Collision with enemy causes Game Over
- Reaching the goal triggers a mini-game
- Mini-game requires clicking colored buttons in the correct order
- Sound effects for win, lose, and shooting
- Start screen, Game Over screen, and restart functionality

### Main Game
- **Move**: Use arrow keys (↑/↓/←/→) to move the player
- **Shoot**: Click anywhere on the screen to shoot a star
- **Objective**: Avoid ghosts and reach the glowing magic circle to enter the Mini Game

### Mini Game
- **Interaction**: Click on the colored buttons in the correct order
- **Round System**: Each round consists of **4 button clicks**
- If the correct sequence is entered, a **You Win** image appears, and the **Exit** button becomes clickable
- If the sequence is incorrect, the round resets and the player can retry

### Game Over
- **Restart**: Click the **Restart** button to replay the game
- **Exit**: Click the **Exit** button to quit

### Requirements
- Android Studio
- JDK 8 or higher
- Gradle (included via wrapper)
- Android SDK (target API 30)
- Emulator: Pixel 2

## License and Assets
- Game assets used are either:
- Created by the team
- Licensed for free commercial use