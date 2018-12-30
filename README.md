# 葫芦兄弟大闹超级碗



## 游戏介绍



### 背景

万恶的蛇精把老爷爷绑架了，并命令蝎子精带领部下把守大门。老爷爷吃下了有毒的饭菜，急需解药！葫芦兄弟必须越过一片橄榄球场，及时将老爷爷救出并让他服下解药。然而，训练有素的蝎子精部队试图拦截葫芦兄弟，他们采取“人盯人”的防守策略，不断试图绕前防守。战斗力最强的蝎子精亲自盯防手上持有解药的兄弟。



### 取胜方法

只要七兄弟中的一个手持解药“达阵”便可成功救出爷爷。然而，所有敌人的大脑中均内置有寻找**最短路径**的芯片，因此他们总能以最快的速度冲向防守目标。想要摆脱防守，有以下两计：

* **走位**。控制方向键**W**(上)、**A**(左)、**S**(下)、**D**(右)可以进行跑动以达到晃开防守人的目的。此法考验手速。
* **传球**。左键点击场上队友的位置可以将解药传递给队友。蝎子精由于要亲自盯防持有解药的兄弟，在观察到传球后会立刻通知相应的小喽喽进行换防，葫芦兄弟可以利用他们换位的时间趁机向前推进。***风险：解药有一定概率会被敌方拦截下来，老爷爷会因此抢救无效直接死掉！你，真的敢冒这个险吗？***



### 战斗效果



![gif](/README/gif.gif)



## 设计方法与技巧



### MVC模式



#### Model

Model模块是游戏的“具体玩法”，在前面作业的基础上继续完善。Model模块与View模块、Controller模块是**松耦合**的，其作为游戏引擎向View提供游戏数据。



![屏幕快照 2018-12-27 下午3.14.35](/README/1.png)



查看Model模块依赖的模块：

![屏幕快照 2018-12-27 下午3.45.44](/README/2.png)

其中不包含View模块和Controller模块，说明Model模块是完全**UI-independent**的。



#### View 



View模块负责界面逻辑的搭建。View从Model中读取需要的数据（如每个Creature的坐标），并将Model数据组织映射成View上的数据，进而刷新UI。View并不关心游戏的逻辑是怎样的，对于View来讲，Model中可见的只有物体的位置信息等：

![屏幕快照 2018-12-28 下午2.04.33](/README/3.png)



#### Controller

Controller模块是Model与View之间的一座桥梁。

![屏幕快照 2018-12-27 下午3.48.32](4.png)



Controller中聚合了KeyEventHandler以及MouseEventHandler，负责监听View上的事件。当Controller接收到View上传来的事件时，Controller根据事件的信息对Model进行相应的更新。因此，View无法直接修改Model，而是让Controller成为Delegate间接修改Model。



#### Main Thread

主线程的工作：

* 创建Model、View、Controller

  ```java
  final Model model = new Model();
  final View view = new View();
  final Controller controller = new Controller(model, view);
  ```

* 设置Controller为View的**Delegate**

  ```java
  view.scene.setOnKeyReleased(controller.keyEventHandler);
  view.canvas.setOnMouseClicked(controller.mouseEventHandler);
  for (Button button : view.buttons)
      button.setOnMouseClicked(controller.mouseEventHandler);
  ```

* 启动Model线程

  ```java
  ExecutorService exec = Executors.newCachedThreadPool();
  exec.execute(model);
  ```

* 启动UI刷新线程，以60fps的频率刷新UI

  ```java
  new AnimationTimer() {
      @Override
      public void handle(long now) {
          view.update();
      }
  }.start();
  ```



这样，View按照自己固定的频率刷新UI，无需关心Model刷新的频率，只管从Model中读取数据；Model中又可以开启多个子线程并发执行。

***使用MVC模式使得模块之间耦合度极大地降低。***





### 观察者模式



既然Model是UI-independent的，那么当Model的状态发生变化并且需要将这个信息告知Controller时，该怎么办呢？

考虑这样一个场景：Model中持球人达阵或传球失败后，意味着游戏结束，这时理应让View弹出一个对话框显示战斗结果。但是Model不可以直接与Model展开对话，那么只能通过Controller这个“中介”了。然而对于Model而言，它甚至不知道Controller是谁，这可咋整呀？



我们可以换个角度来思考：把Model看成一个“电台”，它能够发出广播，而Controller只要调到Model的“频道”上就可以收听“节目”了！

这就是所谓的观察者模式。基于java.util.Observable以及java.util.Observer可以很方便地实现观察者模式（注：Java 9 以后已经被弃用了，因为确实不够灵活；java.beans.PropertyChangeListener是比较好的替代品）:



```java
public class Model extends Observable implements Runnable {
    @Override
    public void run() {
        ...
        if (end()) {             //战斗结束，“广播”，通知自己的观察者
            setChanged();
            notifyObservers();
        }
    }
}

public class Controller implements Observer {
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        model.addObserver(this);    //调到Model的“频道”上
    }
    
    @Override
    public void update(Observable o, Object arg) {    //监听到Model状态发生变化后，更新View
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("葫芦娃胜利!");
                    alert.show();
                }
        );
    }
}
```







### 对象池化



为什么要将对象“**池化**”？

原因很简单：Java在进行垃圾回收时的“Stop-and-Copy”机制会暂停程序的运行，而一旦暂停的时间过长导致玩家可以感知，便会严重影响玩家的游戏体验。因此，应该尽量复用对象，不能频繁地创建销毁对象。

使用对象池一方面可以高效地对对象资源进行**统一管理**，另一方面也有助于**线程安全**。



#### Cell池

代码中将游戏地图上的每个位置Cell对象存入一个Cell池中。可以想象，当地图非常大时，频繁地建立销毁Cell对象将是一件多么耗时的事情！干脆将需要用到Cell对象存进对象池中以供复用。

我们可以将Creature在地图上的移动理解为这样一个过程：每个Creature线程试图从Cell池中获取一个Cell并持有它。当多个Creature同时试图移动到同一个位置时，也就相当于多个线程竞争Cell池中的同一个Cell资源。然而，一个坐标在Cell池中只对应一个Cell对象，因此无法出现多个Creature持有同一个Cell的情况。这保证了多线程状态下Creature的坐标不会发生冲突。



Cell池以**静态内部类**的方式实现为一个**单例**：

```java
private static class CellPoolHolder {
    private static CellPool cellPool = new CellPool();
}

public static CellPool getInstance() {
    return CellPoolHolder.cellPool;
}
```



Cell对象在Cell池中是以“懒汉式”加载的，当一个坐标对应的Cell被请求获取且池中仍未曾创建该Cell时，使用**Factory**创建一个Cell对象：

```java
public class CellFactory extends BaseKeyedPooledObjectFactory<Coordinate, Cell> {

    @Override
    public Cell create(Coordinate coordinate) {
        return new Cell(coordinate);
    }

    @Override
    public PooledObject<Cell> wrap(Cell cell) {
        return new DefaultPooledObject<>(cell);
    }

    @Override
    public void passivateObject(Coordinate coordinate, PooledObject<Cell> pooledObject) {
        pooledObject.getObject().remove();
    }
}
```





### 枚举类型与接口



葫芦娃对象CalabashBrother以及其余生物体NonCalabashBrother均实现为枚举类型。使用枚举类的好处有：

* 生物体均实现为单例，比较符合实际情况
* 可以有效地将一组具有相同性质的生物体组织在一起



#### Delegate模式实现枚举类型的“多继承”

然而，enum的功能是受限的。其中一个用起来不顺手的地方是，由于一个enum默认继承自java.lang.Enum，而java不支持多继承，因此一个enum不能再继承其他任何的类。



一个解决方案是使用**接口**来组织枚举：

```java

public interface Creature {
    ...
}

public enum CalabashBrother implements Creature {
    ...
}

public enum NonCalabashBrother implements Creature {
    ...
}

```

这种做法部分解决了此问题。

然而，接口的使用也是有限制的：接口中定义的属性自动就是static和final的，因此我们无法在接口中定义非静态变量。即便接口将枚举组织了起来，仍然无法在其中定义Creature的属性，这样也就在很多情况下无法在Creature接口中为CalabashBrother和NonCalabashBrother定义一套通用的方法，导致一份相同的代码要在两个枚举类中分别出现。这样**代码复用**的程度太低啦！所以绕了一圈，使用枚举类带来的优点还没它的缺点多？

真的没有办法解决了吗？



终极方案就是**接口** + **Delegate**。

设计一个存放生物体属性的Property类以及一个继承于Property的CalabashProperty类（葫芦娃与正常的生物体相比还多出颜色和排行两个属性）:

```java
class Property {
    private String name;
    private Image image;
    private Cell cell;
    ...
}

class CalabashProperty extends Property {
    private Color color;
    private Rank rank;
    ...
}
```



现在让我们看看CalabashBrother与NonCalabashBrother中的方法是如何工作的：

```java
public enum CalabashBrother implements Creature {
    CalabashProperty property;
    ...
    public void moveDown() throws Exception {
        property.moveDown();
    }

    public void moveLeft() throws Exception {
        property.moveLeft();
    }

    public void moveRight() throws Exception {
        property.moveRight();
    }

    public void moveUp() throws Exception {
        property.moveUp();
    }
}

public enum NonCalabashBrother implements Creature {
    Property property;
    ...
    public void moveDown() throws Exception {
        property.moveDown();
    }

    public void moveLeft() throws Exception {
        property.moveLeft();
    }

    public void moveRight() throws Exception {
        property.moveRight();
    }

    public void moveUp() throws Exception {
        property.moveUp();
    }
}
```

可以看到，具体的工作交由Property类或CalabashProperty类完成。虽然还是存在少量重复代码，但是试想假如具体的工作需要大量的代码完成，那么使用Delegate已经使得代码量精简了不少。



#### 使用default关键字

Java 8中引入了一个新的概念叫做**default**方法,使得可以在接口内部包含默认的方法实现。虽然接口中不能定义非静态变量导致default方法无法对非静态属性进行操作，但是default方法还是能在一定程度上带来便利。比如Creature中有如下用法：

```java
public interface Creature extends Runnable {
    ...
    Cell getCell();
        
    void moveUp() throws Exception;

    void moveDown() throws Exception;

    void moveLeft() throws Exception;

    void moveRight() throws Exception;
    
    default void moveTowards(Creature creature) {
        ...
        if (destination.atUpOf(getCell().getCoordinate()))
            moveUp();
        else if (destination.atDownOf(getCell().getCoordinate()))
            moveDown();
        else if (destination.atLeftOf(getCell().getCoordinate()))
            moveLeft();
        else if (destination.atRightOf(getCell().getCoordinate()))
            moveRight();
    }
}
```



看起来还有点用(^_^)v





### 多线程的协作关系

细数一下程序中开启的线程：

* 主线程

* UI刷新线程
* Model更新线程
* Model中，给每个生物体开启一个线程



其中UI刷新线程与Model线程之间不存在任何依赖关系（前面说过，Model是**UI-independent**的），UI的刷新速率与Model的刷新速率可以不同。

存在协作关系的线程主要是各个生物体线程。事实上，如前文所述，已经通过使用一个Cell对象池的方式保证各生物体的位置不会发生冲突；然而，在我们的游戏玩法中，防守者必须根据得到的防守对象的移动信息反馈来改变自己的位置，否则会出现“漏人”的情况。因此必须设计一种反馈机制，以达到一个生物体在发生位移后通知对应防守人线程的目的。具体来说，使用wait()和notify()完成多个生物体线程之间的通信。

一个葫芦娃线程看起来像是这样的：

```java
    @Override
    public void run() {
        while (true) {
            move();
            try {
                notify();
            } catch(Exception e) {
                ...
            }
            Thread.yield();
        }
```



一个敌人线程看起来像是这样：

```java
        @Override
        public void run() {
            while (true) {
                synchronized (葫芦娃) {
                    try {
                        moveTowards(葫芦娃);
                        葫芦娃.wait();
                    } catch (InterruptedException e) {
                        ...
                    }
                }
            }
        }
```

每个防守人线程都会在自己的防守对象线程上wait()，直到防守对象完成一次移动后notify()唤醒自己，进行下一次的moveForward()，如此循环往复。







## 算法设计



### Dijkstra最短路径算法

在游戏中，防守方采用“人盯人”防守策略，因此防守人在察觉到防守对象位置发生变化时必须迅速作出响应，以最快的速度移动到防守对象的前方（尤其是进行换防时，移动距离可能会特别大）。

因此，有必要采用最短路径算法为每个防守人计算出跑动到防守目标前方的最短路径（而且路径需要动态计算，因为战场上的状态瞬息万变）。



#### 移动策略

* 防守方Creature线程wait自己的防守对象；

* 进攻方Creature发生移动后，notify在该Creature上等待的的防守人；
* 防守人被唤醒，调用Dijkstra算法计算出当前移动到防守对象前方的最短路径；
* 防守人采用贪心策略，每次沿着新计算出的路径移动一个位置。



#### 建模

* 扫描所有生物体，通过生物体的位置获知地图上所有可通过的点（若位置上有生物体则无法通过）
* 扫描所有位置点：对于每个位置点，若与其相邻的位置可通过，则建立一条邻边。该步骤完成后得到一张邻接链表。
* 将邻接链表、起始位置、终点位置传入最短路径算法类中计算得到最短路径。



#### 算法实现

基于邻接链表的方式，配合**HashMap**与**PriorityQueue**可较方便地实现**Dijkstra**算法（这里没有使用邻接矩阵的方式实现是因为考虑到地图较大时邻接矩阵太大，且由于对于每个点来说最多仅有四个点可达，矩阵稀疏度太大）。

此外，考虑到以后或许还有机会用到这个算法，为了方便以后再复用这段代码，决定使用**泛型**来实现以适配不同的对象类型：

```java
public class Edge<T> implements Comparable<Edge<T>> {
    private T vertex;     //vertex适配不同类型，如坐标由二维变成三维时算法也适用
    private Double distance;
    ...
}
```



#### 算法正确性测试

```java
public class ShortestPathTest {
    @Test
    public void testPath() {
        ShortestPath sp = new ShortestPath();
        ShortestPath.Graph<Character> graph = sp.new Graph<>();
        graph.add('s', Arrays.asList(sp.new Edge<>('t', 10.0),
                sp.new Edge<>('y', 5.0)));
        graph.add('t', Arrays.asList(sp.new Edge<>('x', 1.0),
                sp.new Edge<>('y', 2.0)));
        graph.add('y', Arrays.asList(sp.new Edge<>('t', 3.0),
                sp.new Edge<>('x', 9.0), sp.new Edge<>('z', 2.0)));
        graph.add('z', Arrays.asList(sp.new Edge<>('s', 7.0),
                sp.new Edge<>('x', 6.0)));
        graph.add('x', Arrays.asList(sp.new Edge<>('z', 4.0)));
        Object[] path = graph.getShortestPath('s', 't').toArray();
        Assert.assertArrayEquals(path, new Character[]{'t', 'y'});
    }
}
```





## 致谢



感谢两位老师的精彩讲解，也感谢辛苦的助教哥哥。这个课让我学会了“更加面向对象”地思考问题，设计模式方面的知识也让十分受用。一学期下来代码似乎优雅了那么一点点d(^_^o)
