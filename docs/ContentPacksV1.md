# Creating and Updating Content Packs for LandOfSignals V1

This guide provides instructions for creating and updating content packs for version 1 of the LandOfSignals mod, ensuring compatibility and integration with the mod's features and theme.

## Content Pack Structure and Specifications

A content pack for LandOfSignals V1 should include:

- `pack.json`: Contains metadata about the content pack, including name, version, author, and description.
- `assets`: A directory with models, textures, and other necessary assets.

### pack.json Structure

Example `pack.json` file:

```json
{
  "name": "My Custom Content Pack",
  "version": "1.0",
  "author": "YourName",
  "description": "A detailed description of your content pack."
}
```

### Assets Organization

Assets should be organized into subdirectories within the `assets` folder:

- `assets/models`: For 3D models of signals and signs.
- `assets/textures`: For textures applicable to the models.

## Creating a Signal

To create a custom signal:

1. Model your signal using 3D modeling software capable of exporting to the OBJ format.
2. Create and apply textures to your model.
3. Organize your model and textures in the `assets` directory accordingly.
4. Define your signal in a JSON file within `assets/models`.

### Signal Definition Example

Example JSON definition for a signal:

```json
{
  "id": "custom_signal_v1",
  "name": "Custom Signal V1",
  "model": "models/custom_signal_v1.obj",
  "textures": {
    "texture": "textures/custom_signal_v1.png"
  }
}
```

## Best Practices for Content Pack Creation

- Maintain organization within your content pack for ease of management.
- Use descriptive names for models, textures, and assets.
- Thoroughly test your content pack for compatibility and performance.

## Sharing and Community Support

After creating your content pack, you can share it with the LandOfSignals community. Include a README file with installation and usage instructions. Join our [Discord community](https://discord.gg/ykAqHKYjVM) for support, feedback, and to share your content packs with other users.
