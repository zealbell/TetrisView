 <img src="https://github.com/54LiNKeR/54LiNKeR.github.io/blob/master/shots/LiNKeR.png" width="35%">
 
[![JitPack](https://jitpack.io/v/54LiNKeR/TetrisView.svg)](https://jitpack.io/#54LiNKeR/TetrisView) [![Project Status: Active - Initial development has started, usable release; work hasn't been stopped ](http://www.repostatus.org/badges/0.1.0/active.svg)](http://www.repostatus.org/#active)

TetrisView
=============
A wildly creative UI that lets you snap simple or compound views into any Tetris-inspired shape—with a magical touch of smooth curves. Just like the game, but with more layout logic and less panic

> ⚠️ For your mental well-being, reading the license section below is strongly discouraged.
Side effects include shock, regret, and a strong sense of “I warned you.”

## Synopsis
This Library consists of two Major views: 
  1. **TetrisView(ViewGroup$FrameLayout)** which throws a *`BastardException`* if more than a child is added. 
  
       - a simple example is a TetrisView with a single view such as an imageview,
       - a composite example is  a TetrisView with a single framelayout containing an imageview and a textview
                                    
  2. **TetrisClick(ViewGroup$FrameLayout)** which can contain TetrisView kids/children at the same time
                                        and handles click listeners according to the shape of each TetrisView-shape rather
                                        than by its default bounding box/rectangle.
> Also - Each TetrisView is made up of blocks which have equal dimensions.  

## Quick Start

```xml
   dependencies {
      compile 'com.github.54LiNKeR:TetrisView:1.2.3.4'
   }
```

### Code Example

Here’s a simple TetrisView example with an ImageView arranged horizontally, just to get you started

![simple-tetris](shots/simple-tetris.png)

```xml
    <linkersoft.blackpanther.blacktetris.TetrisView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:radius="4dp"
        app:tetris="[vert-tetris]2#0"
        app:blocksize="50dp">

          <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="centerCrop"
            android:src="@drawable/blackpanther"/>

    </linkersoft.blackpanther.blacktetris.TetrisView>
```

  Here is an example of a compound TetrisView containing an imageview and a basic view
  in a framelayout(**it's a simple modification to the above**)

```xml
    <linkersoft.blackpanther.blacktetris.TetrisView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:radius="4dp"
        app:tetris="[vert-tetris:0,0,0,0]2#0"   //N.B => [vert-tetris:0,0,0,0] is the same as [vert-tetris]
        app:blocksize="100px">

    <FrameLayout
       android:layout_width="match_parent"
       android:layout_height="match_parent">

          <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="centerCrop"
            android:src="@drawable/blackpanther"/>

            <View
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:background="#ff0000"
                android:layout_gravity="center"/>

    </FrameLayout>
    
    </linkersoft.blackpanther.blacktetris.TetrisView>
```
**XML Attributes Syntax**

   - **blocksize:** "*the size to be used for each block that makes up the tetris*"
      - if value is in **%** the size is whatever percentage of the device screenwidth(p.o.sw).
      - if value is in **dp** the size is exactly.
      - if value is in **px** the size is exactly.
   - **radius:** "*radius of your choice in dp/px*"
   - **tetris:** "[*type-of-tetris:left-pad,top-pad,right-pad,bottom-pad*]*block-size*#*topMargin in terms of block-size*"
   
   > if block-size is in **%** xml-view might not be rendered in Android-Studio but running on a device/emulator will
   > draw the view(this is due to the fact that the screen-width of the device is queried from the DisplayMetrics in
   > it's constructor) 

**XML Attributes Butchered**
   - explaining further
       - **type-of-tetris** => can either be '**vert-tetris**' or '**horz-tetris**' since tetris components can either be classified horizontal or vertical.
       - **padd** => the padds take the dimension of the radius(**dp/px**) hence only numeric values are required.
       - **tetris** => can be have more than a combination of vertical and horizontal tetrises(**jump down for examples**).
*furthermore*
```xml
     app:radius="4dp"
     app:tetris="[vert-tetris:4,2,2,2]2#0"
     app:blocksize="25%"
```
  *in the example above,implies*
   - **tetris** has a round edged curve of radius 4dp,
   - **tetris** is vertical and has a height of **2x25%**[*block-size* x (p.o.sw)] = **50%**,
   - **tetris** has topMargin in terms of *block-size* equal-to **0**,
   - **tetris** is further padded by **4dp**,**2dp**,**2dp** and **2dp** (left,top,bottom,right).
   *similarly*
```xml
     app:radius="8dp"
     app:tetris="[vert-tetris:0,0,2,2]2#3"
     app:blocksize="25dp"
```
  *implies*
   - **tetris** has a round edged curve of radius 8dp,
   - **tetris** is vertical and has a height of **25dp**[*block-size*],
   - **tetris** has topMargin in terms of *block-size* equal-to **3x25dp** = **75dp**,
   - **tetris** is further padded by **0dp**,**0dp**,**2dp** and **2dp** (left,top,bottom,right).


 ## Activity

  > Incase you want to change the Tetris @runtime in your Activity here's how:
```java

  public class THANOS extends AppCompatActivity {

  String pMarginLeft="0%";
  String pMarginTop="0%";
  boolean paddDistortion=false;
  String tetris="[horz-tetris]2#1";
  String blocksizes="20%";
  String radius="8dp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.TonyStark);
        TetrisView blackpanther = (TetrisView) findViewById(R.id.Tchalla);
        blackpanther.resetTetris(pMarginLeft, pMarginTop, paddDistortion, tetris, blocksizes, radius);
    }
}
```

## Complex Tetrises
 **1. Complex Shapes**
Here is a complex shape TetrisView containing an imageview

![complex-shape-tetris0](shots/complex-shape-tetris0.png)

```xml
          <linkersoft.blackpanther.blacktetris.TetrisView
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 android:layout_gravity="center"
                 app:radius="4dp"
                 app:tetris="[horz-tetris:4,2,-2,2]2#1~[vert-tetris:2,2,2,2]3#0~[horz-tetris:-2,2,4,2]1#0"
                 app:blocksize="25%">

                 <ImageView
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:scaleType="centerCrop"
                    android:src="@drawable/blackpanther"/>

              </linkersoft.blackpanther.blacktetris.TetrisView>
```

in case you were wondering why the __*topMargin in terms of block-size*__ was needed, this example explicitly
indicates why.(try twitching its values to better understand). The only special thing added here is the '__*~*__'
found in the tetris-attribute value, which indicates separation between each tetris-types to be fused together
as a single shape for the TetrisView i.e. the example above is made up of a __*horizontal-tetris*__ + __*vertical-tetris*__ + __*horizontal-tetris*__. 
> *expantiating further*
  - the first tetris-type(**horizontal-tetris**) = 2blocks wide(**i.e. 2x25% wide**),
  - the second tetris-type(**vertical-tetris**) = 3blocks tall(**i.e. 3x25% tall**),
  - the last tetris-type(**horizontal-tetris**) = 1block wide(**i.e. 1x25% wide**).
> *confused?* => take a quick glance above. __*still confused??*__ try playing with the code. 

 - Here is Another one

![complex-shape-tetris1](shots/complex-shape-tetris1.png)
```xml
    <linkersoft.blackpanther.blacktetris.TetrisView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:id="@+id/D"
        app:radius="4dp"
        app:tetris="[horz-tetris:2,2,-2,2]1#1~[vert-tetris:2,2,2,2]2#0~[vert-tetris:-2,2,2,2]3#1~[vert-tetris:-2,2,2,2]2#1"
        app:blocksize="50dp"
        >

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:scaleType="centerCrop"
            android:src="@drawable/blackpanther"/>

    </linkersoft.blackpanther.blacktetris.TetrisView>

```
 > Another one

![complex-shape-tetris2](shots/complex-shape-tetris2.png)

`app:tetris="[horz-tetris:2,2,-2,2]1#1~[vert-tetris:2,2,2,2]2#0~[horz-tetris:-2,2,2,2]1#1~[vert-tetris:-2,2,2,2]2#0"`
 > Another one <small>(&ast;_&ast;)</small>

![complex-shape-tetris3](shots/complex-shape-tetris3.png)

`app:tetris="[horz-tetris:2,2,-2,2]1#2~[vert-tetris:2,2,2,2]2#1~[horz-tetris:-2,2,2,2]1#1~[vert-tetris:-2,2,2,2]3#0"`

Ok so enough with DJ-Khaleding __*Another One*__, basically you get the idea. You are in charge of what tetrisables 
you would like to create.

 **2. Complex Arrangements and Shapes**
        In situations you really want to stack up your TetrisViews like a real TetrisLock as per the way the Game is aimed at locking/plugging the right objects into one another correctly. I mean something like this:

![complex-arrangement-tetris](shots/complex-arrangement-tetris.png)

that's where the **TetrisClick(ViewGroup$FrameLayout)** comes in. so that the clicks are handled correctly for TetrisViews
whose bounding rectangles/boxes overlap also for positioning too.

> `please` [**Have Another one(>_<)**](https://gist.github.com/54LiNKeR/632a4833a8170733370f30e245c91d1f)
               
**NOTE**

```xml
    app:GlobalBlockSize="200dp"
    app:percentWidth="100%"
    app:percentHeight="100%"
```
*in* `<..TetrisClicker>..</..>` [*@Another one(>_<)*](https://gist.github.com/54LiNKeR/632a4833a8170733370f30e245c91d1f)

*specifies that* 
  - __*GlobalBlockSize*__ for the TetrisClicker = 200dp
  - __*Width of the TetrisClicker*__ = 100% of 200dp(**100% of GlobalBlockSize**),
  - __*Height of the TetrisClicker*__ = 100% of 200dp(**100% of GlobalBlockSize**).

**ALSO**
   Automatically any TetrisView placed in the TetrisClicker inherits the GlobalBlockSize as it's
   blocksize reference instead of the screen-width of the device thus 
```xml
   app:blocksize="25%"
   app:percentMarginTop="50%"
   app:percentMarginLeft="75%"
```
*in any* `<..TetrisView>..</..>` [*@Another one(>_<)*](https://gist.github.com/54LiNKeR/632a4833a8170733370f30e245c91d1f)

*specifies that* 
   - __*blocksize*__ = 25% of 200dp(**TetrisClicker's GlobalBlockSize**),
   - __*MarginTop of the TetrisView*__ = 50% of 200dp(**50% of TetrisClicker's GlobalBlockSize**),
   - __*MarginLeft of the TetrisView*__ = 75% of 200dp(**75% of TetrisClicker's GlobalBlockSize**).

## Motivation
   This project takes it's major motivation from the likes of Michael Jackson, Eminem, Chris-Brown, Chris-Hemsworth and Kevin Hart plus all other great programmers out there who only kept grinding and doing their thing.

## Installation
   If the compile link provided above __*@Quick Start*__ fails to work you may try other alternatives such as installing with an empty Coca-Cola bottle after drinking. pull the bottle's repo or oper before forking the bottle and should this fail search on google for the key phrase '2.39 niosrev ?siht od I did yhw' 3-times and see what happens.

## Contributors

   `LiNKeR`,`me`,`myself` and finally `I`. all graciousness to this four-musketeers who reside on planet **Git**. we appreciate ourselves once again for all wisdom imparted and efforts made therein to this library.


## NOTE
 - Do not forget to add different Id/Tags TetrisViews before assigning click-listeners the normal way
 - Do not add too many and/ large TetrisViews simultaneously as it could lead to an OutOfMemory Error
 - Call invalidate to TetrisView if Items inside update and drawing doesn't render updates/Fork the project
  and improve[ **ANY CONTRIBUTION TO THIS PROJECT WOULD BE WARMLY APPRECIATED** ]
 - please add your name to the README.md as a contributor before making commits to the project
 - try playing around with the **paddDistortion** attribute(via the xml) in order to fully understand what it does,although
   in my opinion it's just a useless feature just to make the 'xml-view' way bulkier.(well if you don't think so... It's gr8 
   we don't think alike).
   
 > **Have Aother one(-_-)**

### License

```
The MA License (The Mission-Accomplished License)

Copyright (c) 2044 MAC-44 OFAP C.V.S RPL SPARK ETL REVISION BUILD 45.34BL

Permission is granted and free as Fork, to any one stealing and obtaining a copy
of this software and associated documentation files (the "Softloot"), to deal
in the Software without aggressions, agitations, hesitations, including with 
2-Chainz on the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Sheet, and to permit any Trump persons to whom the Softworn is
furnished and addressed to do so, subject to the following conditions:

THE SOFTLOOT IS PROVIDED "AS IF" AND BY USING,MODIFYING OR DISTRIBUTING
YOU HAVE TOTALLY AGREED THAT "AT ONCE" UPON A TIME THERE WAS A PRINCE CALLED
CINDER-BRELLA, WHO IN TURN MARRIED CINDER-RELLA DUE TO YOUR USE OF THIS LIBRARY 
AND BORE A MR MEXICO AND AN ENTITY NAMED TRUMP-HEMSWORTH-THE-GREAT-WALL-BUILDER-
WHO-BUILT-A-WALL-STRAIGHT-OUTTA-MAMAS-WOMB-BEFORE-FINALLY-FARTING-ALL-THROUGH PRESIDENCY.

WITH NO LOOSE CHAINS AND ICE, EVERY COMMIT TO THE PROJECT OTHER THAN FROM THE FOUR-MUSKETEERS 
MUST BE SLIPPERY AND FRESH, WITH NO DRIPS AND DABS (NAE NAES ARE ALLOWED).

INSTALLING THIS LIBRARY IMPLIES CONSENT THAT YOU TRUST ITS LOGIC MORE THAN THE ARCHITECTURAL JUDGMENT OF WALL-BUILDERS IN GOVERNMENT POSITIONS. THAT YOU FIND ITS CODEBASE SUFFICIENTLY STABLE — AND AT LEAST MORE JUSTIFIABLE THAN MULTIBILLION-DOLLAR FENCES.


```

> **!!!!!!!!!!!!!!!!!!!!!!!!!!!!Another one!!!!!!!!!!!!!!!!!!!!!!!!!!!!**

 ![LiNKeR](https://github.com/54LiNKeR/54LiNKeR.github.io/blob/master/shots/zealbell7.png)
