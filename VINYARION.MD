## Commands

Here follows a complete list of all commands added to the base mod and their usage.

The commands are:
```/siege_list``` - for admins, to list sieges of a given activity and gameplay mode
```/siege_mode``` - for admins, to set the gameplay mode of a siege
```/siege_rule``` - for admins, to add, remove, or otherwise manage gameplay rules
```/siege_zone``` - for admins, to add, remove, or otherwise manage the zones of a siege

NOTE: Use TAB key to autocomplete command parameters.
The character ~ can be used in place of coordinate parameters to refer to the operator's current coordinate, e.g. when setting zone positions.

...

### ```/siege_list```... (for admins)
```
/siege_list active
/siege_list active [gameplay-mode]
/siege_list inactive
/siege_list inactive [gameplay-mode]
/siege_list deleted
/siege_list deleted [gameplay-mode]
```
...
### ```/siege_mode```... (for admins)
```
/siege_mode [siege-name] [gameplay-mode]
/siege_mode [siege-name] [gameplay-mode] [points-required-for-win]
```
...
### ```/siege_rule```... (for admins)
```
/siege_rule [siege-name] [rule-name] add
/siege_rule [siege-name] [rule-name] set-value [value]
/siege_rule [siege-name] [rule-name] remove
```
...
### ```/siege_zone```... (for admins)
```
/siege_zone [siege-name] add [siege-team] [x] [y] [z] [size]
/siege_zone [siege-name] add [zone-name] [x] [y] [z] [size]
/siege_zone [siege-name] set-value [value]
/siege_zone [siege-name] remove [siege-team]
/siege_zone [siege-name] remove [zone-name]
```
