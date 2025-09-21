Admin Password Reset and User Deletion

Adds functionality to the admin home scene to allow them to reset a users password. When the user logs in with the one time password they will be prompted to create a new valid password.
Adds functionality to the admin home scene to allow them to delete other users. Before deleting the user a confirmation prompt will be displayed.

Implementation and How it Works:
Added two buttons to the admin home scene that will then scene transition to either the scene for password reset, or account deletion. For password reset reused code for invitation code to create a 4 character password to store into the SQL table. Password is displayed as an alert. When user logs in with the newly created password they will prompted to create a new valid password.
When deleting a user the scene displays a text box asking for the username to delete. Before deleting the user confirmation box will be displayed asking again to ensure that this is the account you want to delete.
