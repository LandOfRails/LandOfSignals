---
hide:
  - toc
---

# Header (landofsignals.json)

## Description

The first thing you'll need to create a contentpack is the `landofsignals.json`.<br />
It contains all the vitale information needed for LandOfSignals to add your content to the game.<br />
It contains the name, author, packversion and addonversion of your pack as well as the links to the blocks.

!!! info "The first entries in your ZIP file should be `/landofsignals.json` (file) and `/assets/` (directory).<br />There can't be a folder above."

## Structure

``` json linenums="1" title="landofsignals.json"
{
    "name": "My contentpack (1)", // (1)!
    "packversion": "1.0", // (2)!
    "author" : "John Doe", // (3)!
    "addonversion": "2", // (4)!
    "content": { // (5)!
        "assets/landofsignals/folder1/complexsignal.json": "BLOCKCOMPLEXSIGNAL", // (6)!
        "assets/landofsignals/folder2/signal.json": "BLOCKSIGNAL",
        "assets/landofsignals/folder3/deco.json": "BLOCKDECO",
        "assets/landofsignals/folder4/sign.json": "BLOCKSIGN",
        "assets/landofsignals/folder5/signalbox.json": "BLOCKSIGNALBOX",
        "assets/landofsignals/folder6/lever.json": "BLOCKLEVER"
    }
}
```

1.  

    !!! info "The name you want your contentpack to have.<br />Preferably something meaningful and descriptive."
 
    !!! warning "Needs to be unique!<br />If more than one pack has the same name, only one will be loaded."

    !!! danger "Should not be changed!<br />Changing the name will break existing blocks."

2.  
  
    !!! info "The version of your pack.<br />Choose a format of your liking.<br />There is no validation."

3.  
    
    !!! info "The author(s) of the pack. You?<br />No validation as well."

4.  
    
    !!! info "The addon-version used.<br />Should be "2" (latest)"
 
5.  
    
    !!! info "This contains all yours blocks. Can be 1+ entries."

    !!! note "The first folders always have to be `assets/landofsignals/`<br />After that you can choose folders and filenames freely."

6.  
    
    !!! info "Each entry (block) consists of the file path and type of block."

    !!! abstract "Available types:<br />BLOCKCOMPLEXSIGNAL, BLOCKSIGNAL, BLOCKDECO, BLOCKSIGN, BLOCKSIGNALBOX and BLOCKLEVER<br /><br />Overview and further information<br / >[:octicons-arrow-right-24: ContentPacksV2](../ContentPacksV2.md)"














