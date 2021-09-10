# sausages.help
### Review your hostage experience
> The online GTA videgame allows players to take other player hostage for different criminal activities.
> This application is not to be taken seriously, it is done purely for fun and as a display of my software developpement skill and knowledge.
>

## Technology

- Java 16
- Maven
- MySQL
- SprinBoot
    - Spring data
    - Spring security 5
- Vaadin Flow 22


## Local developpement & dependencies

### MySql Database
Local developpement is done using the `local` spring profile.

A MySql database must be available to run the application. By default, it runs on local host, as defined by the Spring property:
```
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/sausage
```
Thus, the environement variable `MYSQL_HOST` can be overriden to use a connection to any MySql instance.
You will need to update credentials accordingly (properties `spring.datasource.username` & `spring.datasource.password`)

### Vaadin

To run, Vaadin requires a recent enough version of [nodejs](https://nodejs.org/en/) 
and [pnpm](https://pnpm.io/) to be installed on the host machine.