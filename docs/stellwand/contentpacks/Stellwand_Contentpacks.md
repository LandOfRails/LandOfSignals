# Creating contentpacks

## Basics

<details open>
<summary>Basics</summary>

> What kind of content is addable?

Everything Block you see can be added through the content system. They themselves are added via the content pack system.

> What type of file will the contentpack be?

Currently a *.zip-File - It's not planned for other file types to be available.

> What do I need to create a contentpack?

- Some Modeller: For creating the *.obj files<br>
- Some text-editor: For creating the adjacent *.json files
- Some Filezipper: For packing the files into a *.zip

What have I used?

- Blockbench
- Notepad++/Visual Studio Code
- WinRar / 7ZIP

</details>

## Basefile

<details>
<summary>Basefile</summary>

```json
{
  // Current addonversion, leave it like this
  "addonversion": "2",
  // Your contentpackname.
  "name": "MyContentpackname",
  // The version of your contentpack
  "packversion": "1.0",
  // The author of this pack
  "author": "You",
  "content": {
    // Path to your block-jsons : Blocktype
    "assets/stellwand/mainpack/signals/blockstraightunisolated/blockstraightunisolated.json": "BLOCKSIGNAL",
    // "assets/stellwand/[MyContentpackname]" is needed. Structure after that is open.
    "assets/stellwand/mainpack/senders/blocksender2/blocksender2.json": "BLOCKSENDER",
    "assets/stellwand/mainpack/fillers/blockfiller/filler.json": "BLOCKFILLER"
  }
}
```

> <h3>*Important:*</h3>
The packId is created from `name` and `author`. This should not be changed after being initially set!
What happens if you still do it? Older blocks loose their reference and will be broken.

<br>Available Blocktypes:

- BLOCKFILLER
- BLOCKSENDER
- BLOCKSIGNAL
- BLOCKMULTISIGNAL

</details>

## Content (Blocks)

<details>
<summary>Content</summary>
<br>

<details open>
    <summary>Type: BLOCKFILLER</summary>

```json
{
  // Name of your block
  "name": "Custom Block Filler",
  // Path to your block *.obj, starts from the contentpackname
  "model": "myContentpackname/fillers/blockfiller/filler.obj",
  "block": {
    // Default rotation of your block (x,y,z)
    "rotation": [
      0,
      0,
      0
    ],
    // Default translation of your block (x,y,z)
    "translation": [
      0.5,
      0.5,
      0.5
    ]
  },
  "item": {
    // Default rotation of your item (x,y,z)
    "rotation": [
      15,
      195,
      0
    ],
    // Default translation of your item (x,y,z)
    "translation": [
      0.5,
      0.5,
      0.5
    ],
    // Scale of your item. Usally smaller than 1
    "scale": 0.7,
    // Path to your item *.obj, starts with contentpackname
    "model": "myContentpackname/fillers/blockfiller/filler.obj"
  }
}
```

</details>

<details open>
    <summary>Type: BLOCKSENDER</summary>

```json
{
  // Name of your block
  "name": "Custom Block Sender",
  // Path to your block *.obj, starts from the contentpackname
  "model": "myContentpackname/senders/blocksender/blocksender.obj",
  "block": {
    // Default rotation of your block (x,y,z)
    "rotation": [
      0,
      0,
      0
    ],
    // Default translation of your block (x,y,z)
    "translation": [
      0.5,
      0.5,
      0.5
    ]
  },
  "item": {
    // Default rotation of your item (x,y,z)
    "rotation": [
      15,
      195,
      0
    ],
    // Default translation of your item (x,y,z)
    "translation": [
      0.5,
      0.5,
      0.5
    ],
    // Scale of your item. Usally smaller than 1
    "scale": 0.7,
    // Path to your item *.obj, starts with contentpackname
    "model": "myContentpackname/senders/blocksender/blocksender.obj"
  }
}
```

</details>

<details open>
<summary>Type: BLOCKSIGNAL</summary>

```json
{
  "name": "Custom Block Straight Unisolated",
  "directionFrom": [
    "LEFT"
  ],
  "directionTo": [
    "RIGHT"
  ],
  "model": "mainpack/signals/blockstraightunisolated/unisolatedtrack.obj",
  "block": {
    "rotation": [
      0,
      0,
      0
    ],
    "translation": [
      0.5,
      0.5,
      0.5
    ],
    // Different signalstates
    "modes": {
      // Groupname: Groupid (Should both be unique, Id is used for .obj)
      "Off": "off",
      "White": "white"
    }
  },
  "item": {
    "rotation": [
      15,
      195,
      0
    ],
    "translation": [
      0.5,
      0.5,
      0.5
    ],
    "scale": 0.7,
    "model": "mainpack/signals/blockstraightunisolated/unisolatedtrack.obj",
    "mode": "white"
  }
}
```

</details>

<details open>

<summary>Type: BLOCKMULTISIGNAL</summary>

```json
{
  "name": "Custom Multiblock Straight and Diagonal",
  "directionFrom": [
    "LEFT",
    "TOP_LEFT"
  ],
  "directionTo": [
    "RIGHT",
    "BOTTOM_RIGHT"
  ],
  "model": "mainpack/multisignals/blockstraightanddiagonal/blockstraightanddiagonal.obj",
  "block": {
    "rotation": [
      0,
      0,
      0
    ],
    "translation": [
      0.5,
      0.5,
      0.5
    ],
    // Different signals
    "modes": {
      // Signalname
      "Left track": {
        // Groupname: Groupid (Should both be unique, Id is used for .obj)
        "Off": "leftStraightOff",
        "White": "leftStraightWhite",
        "Red": "leftStraightRed"
      },
      "Left top track": {
        "Off": "leftTopOff",
        "White": "leftTopWhite",
        "Red": "leftTopRight"
      },
      "Right track": {
        "Off": "rightStraightOff",
        "White": "rightStraightWhite",
        "Red": "rightStraightRed"
      },
      "Right bottom track": {
        "Off": "rightBottomOff",
        "White": "rightBottomWhite",
        "Red": "rightBottomRed"
      }
    }
  },
  "item": {
    "rotation": [
      15,
      195,
      0
    ],
    "translation": [
      0.5,
      0.5,
      0.5
    ],
    "scale": 0.7,
    "model": "mainpack/multisignals/blockstraightanddiagonal/blockstraightanddiagonal.obj",
    "mode": "white"
  }
}
```

</details>

</details>

## Example pack

See [Resources/stellwand](../../../Resources/stellwand/)