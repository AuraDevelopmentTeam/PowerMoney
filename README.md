# [PowerMoney](https://github.com/AuraDevelopmentTeam/PowerMoney)

[![Build Status](https://gitlab.project-creative.de/AuraDev/PowerMoney/badges/master/build.svg)](https://gitlab.project-creative.de/AuraDev/PowerMoney/commits/master)
[![Coverage Report](https://gitlab.project-creative.de/AuraDev/PowerMoney/badges/master/coverage.svg)](https://gitlab.project-creative.de/AuraDev/PowerMoney/commits/master)

A mod/plugin that allows players to earn money with Energy

## Downloads

You can download all builds from either:

- Ore: https://ore.spongepowered.org/AuraDevelopmentTeam/Power-Money
- Curse: https://minecraft.curseforge.com/projects/powermoney
- Maven:
  - Releases: https://maven.project-creative.de/repository/auradev-releases/
  - Snapshots: https://maven.project-creative.de/repository/auradev-snapshots/

## [Issue Reporting](https://github.com/AuraDevelopmentTeam/InvSync/issues)

If you found a bug or even are experiencing a crash please report it so we can fix it. Please check at first if a bug report for the issue already
[exits](https://github.com/AuraDevelopmentTeam/PowerMoney/issues). If not just create a [new issue]
(https://github.com/AuraDevelopmentTeam/PowerMoney/issues/new) and fill out the form.

Please include the following:

* Minecraft version
* Inventory Sync version
* Sponge version/build
* Versions of any mods/plugins potentially related to the issue
* Any relevant screenshots are greatly appreciated.
* For crashes:
  * Steps to reproduce
  * latest.log (the FML log) from the log folder of the server

*(When creating a new issue please follow the template)*

## [Feature Requests](https://github.com/AuraDevelopmentTeam/PowerMoney/issues)

If you want a new feature added, go ahead an open a [new issue](https://github.com/AuraDevelopmentTeam/PowerMoney/issues/new), remove the existing form and
describe your feature the best you can. The more details you provide the easier it will be implementing it.  
You can also talk to us on [Discord](https://discord.me/bungeechat).

## Developing with our Plugin

So you want to add support or even develop an addon for our plugin then you can easily add it to your development environment! All releases get uploaded to my
maven repository. (Replace `{version}` with the appropriate version!)

### Maven

    <repositories>
        <repository>
            <id>AuraDevelopmentTeam/id>
            <url>https://maven.project-creative.de/repository/auradev-releases/</url>
            <!--<url>https://maven.project-creative.de/repository/auradev-snapshots/</url>-->
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>dev.aura.powermoney</groupId>
            <artifactId>PowerMoney</artifactId>
            <version>{version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

### Gradle

    repositories {
        maven {
            name "AuraDevelopmentTeam"
            url "https://maven.project-creative.de/repository/auradev-releases/"
            // url "https://maven.project-creative.de/repository/auradev-snapshots/"
        }
    }

    dependencies {
        provided "dev.aura.powermoney:PowerMoney:{version}"
    }

## Setting up a Workspace/Compiling from Source

* Clone: Clone the repository like this: `git clone --recursive https://github.com/AuraDevelopmentTeam/PowerMoney.git`
* IDE-Setup: Run [gradle] in the repository root: `./gradlew installLombok <eclipse|idea>`
* Build: Run [gradle] in the repository root: `./gradlew build`. The build will be in `build/libs`
* If obscure Gradle issues are found try running `./gradlew cleanCache clean`

## Development builds

Between each offical release there are several bleeding edge development builds, which you can also use. But be aware that they might contain unfinished
features and therefore won't work properly.

You can find the builds here: https://gitlab.project-creative.de/AuraDev/PowerMoney/pipelines

On the right is a download symbol, click that a dropdown will open. Select "build". Then you'll download a zip file containing all artifacts including the
plugin jar.

## Signing

### PGP Signing

All files will be signed with PGP.  
The public key to verify it can be found in `keys/publicKey.asc`. The signatures of the files will also be found in the maven.

### Jar Signing

All jars from all official download sources will be signed. The signature will always have a SHA-1 hash of `2238d4a92d81ab407741a2fdb741cebddfeacba6` and you
are free to verify it.

## License

PowerMoney is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.html)

## Random Quote

> There are two hard things in computer science: cache invalidation, naming things, and off-by-one errors.
>
> -- <cite>Phil Karlton</cite>
