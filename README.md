# Taskr

A project management tool that incorporates everything I've learned about working on software in various teams. Along with learning the expertise of team members and assigning relevant tasks to the right people, Taskr takes into account everyone's workload to plan tasks, easily tracks time spent on projects, and simplifies things like: sprint planning, daily standup meetings, meeting SLA standards, and following up on replies.

Primary technologies used are Kotlin, Java 8, SASS, JOOQ and jQuery. It runs as a self-contained JAR with Spring Boot on AWS. You can see a running demo of the latest minor release at: http://taskr.us-west-2.elasticbeanstalk.com:8080

Take note: This project is an alpha-stage (feature incomplete) work in progress. It is not ready for use in a production environment. Flyway database scripts may change drastically as the project moves into further stages of development. Expect frequent commits.

## Implemented features

* Task list with overload, in-progress, importance tagging
* Basic sprint screen
* Save sprint domain and build repository
* User details/security input
* New issue tagcloud interpretation
* User knowledge tagcloud
* Auto-assignment of new issues and at sprint startup
* In progress tracker, on all pages
* Search issues
* Pulling mechanism for counters and dynamic live issue/comment loading
* Basic chat/comment functionality
* Reponsive layouting
* Dynamic importance sorting
* Categorizing new issues in to "backlog, needs more info or critical"
* Sprint info/backlog
* Sprint closing/starting
* Project switching
* Visualize current project
* Seperation of task list into "My issues, backlog, team issues"
* Basic project page
* Workload interpretation
* *  Setting average work day: which days, how long (on user), sprint length (on project)
* *  Reconfiguration sprint (based on importance)
* Arrive on the project with no active project, force the user to start new project
* Seperate page for shareholders

### Upcoming milestone plan
* Project settings for new and existing projects, such as sprint length and project timezone
* Setup issues
* Milestones/Branching
* Intro page (html with check if online)
* Project, invite people through unique, timed, link
* Activate user
* Login honeypot for bots
* Fix versions/progress in different milestones
* Follow-up (auto-reminder after inaction)
* Standup meeting daily or weekly (if any)
* Security rules via interceptors
* AJAX call 404 error follow-up (refresh)
* jStorage updates via tabs in pulling (50%)
* New issue button everywhere
* Similar issues
* Issue change log
* Timezone setting for issues
* History tab
* Option to redistribute work when working on development/on holiday
* Canned responses (automatically?)
* Comments: Role, see discuss.kotlinlang.org, avatars?
* Rework sprint page

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - Application framework used
* [JOOQ](https://www.jooq.org/) - Type-safe, database first DSL for SQL queries (highly recommended alternative to ORMs)
* [BoneCP](http://jolbox.com) - Fast, open-source, Java database connection pool
* [Stanford Language Processing Libraries](https://nlp.stanford.edu/software/) - Greatly increases the accuracy of the internal keyword generation
* [Apache ActiveMQ](http://activemq.apache.org/) - Open source messaging server, integrated into Spring Boot
* [Kotlin](https://kotlinlang.org/) - Statically typed, concise and safe programming language
* [JavaFaker](https://github.com/DiUS/java-faker) - Fake data generator for realistic testing
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Until version 1.0.0, there are no concrete plans to open up development to non-core teammembers. If you feel contributions are neccesary, feel free to contact Kevin Deyne. Once version 1.0.0 is ready for production release, the project will be available open-source and development will be opened up in the interest of feature requests.

## Authors

* **Kevin Deyne** - *Lead development* - (https://github.com/kevindeyne)

## License

This project is licensed under the Mozilla Public License 2.0 - see the [LICENSE.md](LICENSE.md) file for details
