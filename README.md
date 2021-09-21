# Thesis app src code

Code might not be the cleanest and doesn't follow the greatest conventions for Kotlin (null checking etc..). This is mainy because this project is mainly for trying out different things with Compose and getting to know how the generaly development work feels with it


- [app-debug.apk](https://github.com/arttujo/thesis/raw/master/app-debug.apk) can be downloaded and instealled to try the app. Not 100% sure but might require Android permission to allow the installation of apps from unknown sources

Known Issues:
- Inputs with length limits sometimes reset 
- Some weird issues with transition but this migth be duo to experimental APIs
- Because of poor choises regarding ViewModel/Repo implementation, implementing automatic reloads for some data would have required major refactoring. Implemented swipe to refresh for Home list and profile review list to mitigate this.
- In most cases error handling was left out 
- Didn't implement load more feature for the Rawg.Io api 
- Didn't implement liking feature since it would be a b**** to implement while using Json Server 


In case the main API (ran on a personal server from home) for the app is down and there's a logged in user DataStore the home page will show an error with the API error

# Design
There has been some changes inside the app to this and additions. And as you can see.. I'm not a great designer..
![wireframe](https://user-images.githubusercontent.com/16870001/134231646-8b62bb00-d649-4f85-8dd6-6078340d79ab.png)
