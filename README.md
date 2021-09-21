# Thesis app src code

Code might not be the cleanest and doesn't follow the greatest conventions for Kotlin (null checking etc..). This is mainy because this project is mainly for trying out different things with Compose and getting to know how the generaly development work feels with it


Known Issues in app:
- Inputs with length limits sometimes reset 
- Some weird issues with transition but this migth be duo to experimental APIs
- Because of poor choises regarding ViewModel/Repo implementation, implementing automatic reloads for some data would have required major refactoring. Implemented swipe to refresh for Home list and profile review list to mitigate this.
