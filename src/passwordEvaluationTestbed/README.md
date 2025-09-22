# CSE360 – Logout/Role Switching  (HW1)

As a user, I can log out when I’m finished or when I want to switch roles.

## What’s here
- `src/app/auth/AuthSession.java` – tiny session manager (`login`, `logout`, `getRole`).
- `src/passwordEvaluationTestbed/PasswordEvaluationTestingAutomation.java` – console menu:
  1. Run password tests  
  2. Logout (optionally login again)  
  3. Exit  
  4. Change role (ADMIN / USER / GUEST)

## How to run (Eclipse)
1. Open the project **Group-Project-CSE360**.
2. Right-click `PasswordEvaluationTestingAutomation.java` → **Run As ▸ Java Application**.
3. Use the menu to run tests, logout/login, and change role.

## Notes we fixed
- Added a simple logout flow so protected actions require a logged-in user.
- Allowed role switching after logout/login.

## Contributing
- Work on a branch (e.g., `logout-task/<name>`), push to origin, and open into `main`.

