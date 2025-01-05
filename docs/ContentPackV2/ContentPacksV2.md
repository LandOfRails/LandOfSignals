# Creating your own V2 contentpacks

Your contentpacks is made up from multiple parts, you can find a short description and the documentation here:

<div class="grid cards" markdown>

-   :material-file:{ .lg .middle } __landofsignals.json__ *(required)*

    ---

    This file contains the name, author, version of your contentpack.<br />
    It also contains links to your blocks or contentsets.

    [:octicons-arrow-right-24: Header documentation](./Content/Header.md)

</div>


<div class="grid cards" markdown>

-   :vertical_traffic_light:{ .lg .middle } __Signal__

    ---

    ![](signal.png){ width="300" }

    This is a simple signal, it has a more strict and simpler structure.<br />
    It is more performance-friendly and requires fewer lines.<br />
    It has only one controllable group and the model can only be changed via its texture.<br />
    If this suits your needs, this is the preferable option.

    [:octicons-arrow-right-24: Signal documentation](#)

-   :material-traffic-light:{ .lg .middle } __Complex signal__

    ---

    ![](complex_signal.png){ width="300" }

    This is a complex signal, its flexible and opens a lot of options.<br />
    It is less performance-friendly and needs a lot more lines.<br />
    It can have multiple independently controllable groups.<br />
    The state can be changed via texture, obj-groups or with different objs.<br />
    If you have a model/multiple models or want to create a complex block, this is your best option.

    [:octicons-arrow-right-24: Complex signal documentation](#)

-   :material-traffic-cone:{ .lg .middle } __Asset/Deco__

    ---

    ![](deco.png){ width="300" }

    Blocks that are not supposed to have any functionality can be added as a DECO block.<br />
    They have their own creative tab.

    [:octicons-arrow-right-24: Asset/Deco documentation](#)

-   :material-sign-caution:{ .lg .middle } __Sign__

    ---

    ![](sign.png){ width="300" }

    Signs are similar to assets/deco blocks, they have their own creative tab.<br />
    They are supposed to support text-input in the future. *(TBD see #120)*
    
    [:octicons-arrow-right-24: Sign documentation](#)

-   :material-door-sliding:{ .lg .middle } __Signalbox__

    ---

    ![](signalbox.png){ width="300" }
-

    Signalboxes are similar to assets/deco blocks.<br />
    They can be connected to (complex) signals to control them.
    
    [:octicons-arrow-right-24: Sign documentation](#)

-   :material-electric-switch:{ .lg .middle } __Lever__

    ---

    ![](lever.png){ width="300" }

    The newest addition: Levers!<br />
    If you want to add a custom lever/switch that emits redstone when active, this is it.<br />
    It is similar to the signs and assets/deco - difference: You have to state an active and inactive state.

    [:octicons-arrow-right-24: Lever documentation](#)

</div>

<div class="grid cards" markdown>

-   :material-flare:{ .lg .middle } __Flares__ *(optional, nice to have)*

    ---

    ![](flares_day.png){ width="300" }
    ![](flares_night.png){ width="300" }

    If your model contains lamps or other parts that are supposed to glow, this is the right place.<br />
    Flares are oriented to your models and light up when the right state is set.<br />
    They can be added to signals, complex signals, deco/assets, signs and levers.

    > *Documentation to be created (#133)*

</div>

<div class="grid cards" markdown>

-   :material-format-list-group:{ .lg .middle } __Contentsets__ *(optional)*

    ---

    This is its own file containing more blocks.<br />
    It does not contain any logic, the only advantage is that you can group different blocks in your contentpack that belong together.<br />
    It can help with keeping an overview, depending on the amount of blocks you want to add.

    [:octicons-arrow-right-24: Contentset documentation](#)

</div>