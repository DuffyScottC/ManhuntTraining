# Structure

The command starts with the `/train` command manager. This command manages all subcommands (e.g. `/train start` or `/train opt`) which in turn have their own arguments. (e.g. `/train start craft`).

## Sub Commands

### Description
Sub commands are called when the player passes the first argument to the command manager (e.g. `/train <subcommand>`).

### Add a new sub command
To add a new sub command:
- create a new class that implements the `SubCommand` interface
    - (you can also implement the `SubTabCommand` interface, if you want the SubCommand to have tab completion for its arguments.) 
- Create the new class in the `subcommands` package.
- Add an instance of the new class in the `subCommands` map in the `TrainCommandManager`'s constructor with the subcommand's name as the key.
  ```
  subCommands.put("<command name>", new <command class>())
  ```
- `onComand()` is called when the player executes the sub command
- `onTabComplete()` is called when the player tries to pass arguments to the sub command

## Activities

### Description
An Activity is a given trial in the training regimen. For example, the *Crafting* Activity tests a player's ability to craft items. The *Speed Bridge* Activity tests the player's ability to speed bridge over a gap.

### Adding a new Activity
To add a new activity:
- create a new package for your activity in the `activities` package.
- Create a new class that implements the `Activity` interface
- Add a new instance of the class to the `activities` map in the `ActivityManager`'s constructor, with the Activity's name as the key. 
- implement the methods from the `Activity` interface
  - `start()` is called when the `Activity` is called by the `ActivityManager`. It kicks off the activity.
  - `stop()` is called when the activity is done. Used for cleaning up the Activity (e.g. resetting areas, changing player's inventory, etc.). 
  - `isActive()` lets any associated `Listener` classes know whether this Activity is active or not.

### Making an activity configurable
Activities often need configuration options (e.g. a start position to teleport the player to, or an area to detect events in.) 

To make an Activity configurable: 
- make the `Activity` class extend the `ConfigurableActivity` abstract class.
- Create a new `ActivityConfigurer` class for each configuration, as described [below](#using-activity-configurers)

#### Using Activity Configurers
Activity configurers are used to configure individual aspects of an activity. They are usually used to handle the storage of persistent attributes in the plugin's `config.yaml` file. 

To create a new Activity configurer for an `Activity` that extends the `ConfigurableActivity` abstract class:
- create a new class that implements the `ActivityConfigurer` interface for each configuration option. Place it in the `<activity package>/configurers` package.
- add a new instantiation of the `AcivityConfigurer` class to the `ConfigurableActivity` child class's `configurers` map using the `AcivityConfigurer`'s name as the key.

  









