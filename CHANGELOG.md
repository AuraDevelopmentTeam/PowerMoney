Version 1.7.1
-------------

\* Fixed crash when IC² is missing (Fixes [#14](https://github.com/AuraDevelopmentTeam/PowerMoney/issues/14)).  


Version 1.7.0
-------------

\+ Added API for adding MoneyInterfaces.  
\+ Added economy support for Ender Pay.  
\+ Added economy support for Grand Economy.  
\+ Added energy support for BuildCraft.  
\+ Added energy support for IC².  
\+ Added energy support for RedstoneFlux.  
\* Rewrote MoneyInterface system to allow support for more than just Forge.  
\* Feniksovich: Updated ru_RU.lang.  
\* Fixed URLs in docs.  


Version 1.6.1
-------------

\+ Added The One Probe integration.  
\* Only showing one line in Hwyla when the PowerReceiver cannot receive energy.  


Version 1.6.0
-------------

\+ Added Hwyla support.  
\+ Whitebrim: Added root calculation method. (If you adjusted the calculation values check the config. It has been changed a little!)  
\+ Whitebrim: Added calculation offset/shift to further adjust the calculation.  
\+ Whitebrim: Added Russian translation.  
\* Several performance optimizations on the client and server.  


Version 1.5.0
-------------

\+ PowerReceiver can now be turned off with redstone.  
\* Fixed Discord link in [README.md](README.md).  


Version 1.4.3
-------------

\* Fixed opening the Easter Egg crashing the server for real this time (Fixes [#5](https://github.com/AuraDevelopmentTeam/PowerMoney/issues/5) and
  [#6](https://github.com/AuraDevelopmentTeam/PowerMoney/issues/6)).  


Version 1.4.2
-------------

\* ~~Fixed opening the Easter Egg crashing the server (Fixes [#5](https://github.com/AuraDevelopmentTeam/PowerMoney/issues/5)).~~  


Version 1.4.1
-------------

\+ No Easter Eggs added.  
\* Internal optimizations.  


Version 1.4.0
-------------

\* Internal improvements that should significantly improve performance (Changed `BigInteger` to `long`).  
\* Removed all `...String` methods from the CC and OC interface, since they are no longer needed.  
\* PowerReceiver now has a limit of 9,223,372,036,854,775,807 FE/s/player.  


Version 1.3.2
-------------

\+ cdcp998: Added zh_CN.lang (This time for real).  


Version 1.3.1
-------------

\+ ~~cdcp998: Added zh_CN.lang (Didn't work).~~  
\* Fixed some missing metadata.  


Version 1.3.0
-------------

\+ Added OpenComputers support.  
\* Fixed issues with CC and OC integration that when the energy was 0 the methods would just return an error.  
\* Fixed an issue where the CC and OC method `calculateEarnings` would only accept a string instead of a string and an integer.  
\* Slight improvements to CC and OC errors messages.  


Version 1.2.0
-------------

\+ Added a field for the energy consumption of the current block in the GUI.  
\+ Made that value accessible through the CC peripheral.  
\+ Added a method to simulate the earnings for any given energy value.  
\* Fixed meta files.   


Version 1.1.1
-------------

\* Fixed automatic release.  
\* Fixed several SpotBugs warnings.   


Version 1.1.0
-------------

\+ Added ComputerCraft support.  
\+ Added the PowerReceiver as an peripheral of ComputerCraft.  
\* Fixed Tesla support.  


Version 1.0.1
-------------

\+ Added Tesla support.  


Version 1.0.0
-------------

\+ Added PowerReceiver with GUI, textures and recipe.  
\+ Added energy consuming functionality to the PowerReceiver.  
\+ Implemented interface to Sponge so that the generated money can be assigned to the player.  
\+ Added basic config.  


Version 0.0.0
-------------

\* Initial commit  
