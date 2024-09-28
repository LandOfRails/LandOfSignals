### Release 1.3.3

* Fixed [#159] : Mod crashed with java version 12 or higher due to "javalangnosuchfieldexception modifiers"
* Fixed [#159] : Blocks would glitch in 1.14 - 1.16 after the GUI was opened on the signal or the signalbox. This resulted in the block not reacting to changed states and staying rendered even after the destruction of the block.

### Release 1.3.2

* Fixed [#151] : Rail switch always points left from either perspective
* Added [#151] : Added three more versions of the switch stand


* Added : SH2 with light as top and full version
* Improved : LF1 model (fixed lights)

### Release 1.3.1

* Fixed [#143] : Manipulator didn't work on multiplayer (NullPointerException on client)

### Release 1.3.0

We have officially released LandOfSignals 1.3.0 supporting the new UMC 1.2.0 version.

* Added [#140] : Preparations for official release supporting UMC 1.2

* Fixed : The issues regarding the lang-files not working with 1.14 and above.

* Removed : The LandOfSignalsAPI as it was deprecated by now and broke the builds for 1.17 and above (not yet working!)

#### Release 1.2.0

* Added [#125] : Levers w/o animation (extendable via contentpacks)


* Fixed [#127] : Magnifyingglass crashes server

### BACKUP YOUR WORLD BEFORE UPDATING!
#### Release 1.1.0

Fixed #109 : ContentPackComplexSignal can now be added to a contentpackset

Improved #88 : Removed redundant cache

Added #91  : 90 degree variants for stellwand blocks\
Added #98  : Preloading option for models (see config)\
Added #103 : Reimplemented ItemManipulator\
Added #106 : Reimplemented MagnifyingGlass\
Added #108 : Implemented dynamic scaling (see config)

#### Release 1.0.4

#### Alpha 1.0.4-UMC-testbuild

Fix crash due to some contentpacks still containing null. Improved logic to prevent this from happening again.
Fixed missing logging parameter in ContentPackHandlerV2#load

#### Alpha 1.0.3-UMC-testbuild

This build is compatible with current UMC testbuilds in combination with Immersive Railroading testbuilds.

We encourage you to backup your world before updating to this build as it is not excluded that there are still major
bugs in it. Please report any bugs you find to our [issue tracker](https://github.com/LandOfRails/LandOfSignals/issues)
or our [discord](https://discord.gg/ykAqHKYjVM).

#### Release 1.0.3

Fixed #82 | 1.7.10 - 1.16.5:\
Not loaded signals made the client crash once it got updated by the server.

#### Release 1.0.2

Fixed #76 | 1.7.10 - 1.16.5:\
Switching connection to another signalblock wont crash the game after trying to open the signalbox afterwards

#### Release 1.0.1

Fixed #72 | 1.7.10:\
Items are now available in their creative tab

#### Release 1.0.0

* Changed literally everything

---

* New (contentpack) signs
* New (contentpack) decoblocks
* New contentpack-system v2
* Added mapper for contentpack-system v1
* Fixed some bugs
* Deprecated stellwand
