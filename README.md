## 一、前言 ##
在日常的Android开发中，如果不有意地进行架构设计，往往代码都会比较凌乱，其中最常见的一个问题就是Activity的代码太过冗杂，一些复杂的类可能会到达几千行代码。要解决这个问题，可以借助MVP思想对代码进行简单的分层。

## 二、MVP的演变过程 ##
### 阶段一：野蛮生长 ###
在刚接触Android的时候，我们按照官方指引文档还有示例代码，最简单直接地把布局在xml文件里面实现，而View的绑定和其他逻辑都在Activity中实现，任由其野蛮生长，一个Activity走天下。那么随着功能的增加，代码也会变得越来越多，那么就会导致Activity里面的代码非常繁杂，对功能迭代和维护都十分不友好。

### 阶段二：MVC引入 ###
众所周知，MVC架构是源于Web开发演变出来的架构，所以在一开始的时候，Android Develpers很自然地会把这一套架构搬到Android上面来，但是好景不长，开发者们发现使用了这个架构，Activity层的代码还是会变得非常繁杂，原因很简单，“橘生淮南则为橘，橘生淮北则为枳”，没有什么架构是一套走天下的，MVC适用于Web开发，但是在Android开发中就不奏效了，那么是为什么？

![](https://ae01.alicdn.com/kf/H02112660925b4512ae209135a24b11a9S.jpg)

如上图为MVC的结构，模型(model)－视图(view)－控制器(controller)。在Android中，MVC的对应关系如下：

- 视图层(View)：一般采用XML文件进行界面描述
- 控制层(Controller)：对应Activity或Fragment
- 模型层(Model)：数据操作和管理的类

但是在Android中，Activity和Fragment并不是标准MVC模式中的Controller，它的首要职责是加载布局和初始化界面，伴随着控件绑定和触摸事件的分发和处理。所以随着界面和逻辑的不断增加，Activity和Fragment就会变得异常臃肿。

### 阶段三：MVP架构诞生 ###

由于MVC架构并不适合于Android开发，所以开发者们将MVC进行了演进，MVP架构呼之欲出，从此走上了历史的舞台。那么MVP和MVC有什么不一样呢？

![image](https://ae01.alicdn.com/kf/H0f8f12137b0a4cbeac34b8dff08a7e3fE.jpg)

- View层：对应XML和Activity、Fragment，主要负责显示界面和触摸时间分发处理，和Presenter进行交互
- Model层：主要是负责业务逻辑，包括存储、网络等
- Presenter层：属于V、M的中间桥梁，完成它们之间的交互，初始化和释放等工作。

这样，在Activity和Fragment中就可以专一展示View的指责，把和界面不相关的逻辑放到Model层，通过Presenter进行传递和响应，实现了界面和逻辑的分层。

## 三、代码设计 ##

网络上已经有很多关于MVP的代码介绍，但是在项目中使用MVP就需要把MVP抽取成通用的代码，方便其他人进行复用，这也是从架构的思想来设计MVP。

根据`依赖倒置原则`知道，我们需要面向接口编程，不依赖具体实现，所以把MVP的每一层都分离出接口，如下图所示的IView、IPresenter、IModel。

![image](https://ae01.alicdn.com/kf/Ha5db29196bf7452f940edcacc8b0e006s.jpg)

在抽象出来接口之后，那么就需要实现M、V、P层的实现类（或者抽象），其实就是根据二中的MVP结构图把三者连接起来。

### IView的实现类 ###

先看IView的实现类BaseActivity（BaseFragment也是同理），它是直接跟IPresenter交互的，所有它应该持有一个IPresenter的实现类的对象，为了把IPresenter更加抽象，我们用到泛型P来定义这个对象，同时在onDestory的时候对presenter进行释放，避免内存泄漏。

这里的getLifeCycle并不是复写AppCompatActivity的方法，而是在IView中定义了这个接口，具体作用后面再说。

所以整体代码也是非常简单，就是持有一个泛型P的对象，具体创建需要在子类里面进行，从而View的基类就实现好了。

```

public class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {
    @Nullable
    protected  P mPresenter;

    @Override
    public Lifecycle getLifeCycle() {
        return super.getLifecycle();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
```

### IPresenter的实现类 ###

再看IPresenter的实现类BasePresenter，它里面也是严格按照MVP的结构图所示，它能跟V层和M层打交道，所以它持有了泛型V和泛型M的对象。由于Presenter在View层创建，所以它持有的View对象就在构造函数中传入，而持有Model对象则由子类来进行初始化。在onDestroy中对这两个对象进行释放。

```

public abstract class BasePresenter<V extends IView, M extends IModel> extends LifeCycleComponent implements IPresenter {

    @Nullable
    protected V mView;
    @Nullable
    protected M mModel;

    public BasePresenter(@NonNull V view) {
        super(view.getLifeCycle());
        this.mView = view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mView != null) {
            mView = null;
        }
        if (mModel != null) {
            mModel = null;
        }
    }
}
```

### IModel的实现类 ###

最后看IModel的实现类BaseModel，同理，按照MVP结构图，Model层只持有Presenter的对象，而Model是由Presenter进行示例化，所以它持有的Presenter对象也是在构造函数中传入。在onDestroy中进行释放Presenter对象。


```
public class BaseModel<P extends IPresenter> extends LifeCycleComponent implements IModel{
    @Nullable
    protected P mPresenter;

    public BaseModel(@Nullable Lifecycle mLifeCycle, @Nullable P presenter) {
        super(mLifeCycle);
        this.mPresenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPresenter != null) {
            mPresenter = null;
        }

    }
}
```

### LifeCycle辅助类 ###

通过看上面的代码，大家可能还有疑问，如IView定义的getLifeCycle方法作用是什么？为什么BasePresenter和BaseModel都继承了LifeCycleComponent？BaseModel的构造函数为什么要传入Lifecycle对象？

不用急，为您一一解答。

首先了解一下什么是LifeCycle，简单来说就是在Activity（Fragment）以外的组件用来监听Activity（Fragment）生命周期的一个架构组件，从而让该组件拥有和Activity（Fragment）一样的生命周期，它的原理也是比较简单，最新包的Activity（Fragment）都实现了一个LifecycleOwner的接口，只要我们在需要的组件里面实现LifecycleObserver接口，然后在Activity（Fragment）调用lifecycle.addObserver(LifecycleObserver)即可，其实就是一个监听者模式。

了解了LifeCycle之后，那么我们为什么要用它呢？因为Presenter和Model其实就是一个外部组件，为了避免内存泄漏，我们必须让它们保持和Activity（Fragment）一样对生命周期，在Activity（Fragment）被销毁时候，这两个组件都能释放资源。所以LifeCycleComponent就是一个实现了LifecycleObserver接口的组件，然后弱引用持有传入的IView（因为它提供了getLifeCycle方法），通过IView拿到lifecycle，然后把自己和Activity（Fragment）关联起来，所以BasePresenter和BaseModel就可以在内部管理生命周期。


```
public class LifeCycleComponent implements LifecycleObserver {
    @Nullable
    private WeakReference<Lifecycle> mLifeCycle;

    public LifeCycleComponent(@Nullable Lifecycle mLifeCycle) {
        this.mLifeCycle = new WeakReference<>(mLifeCycle);
        addLifeCycle();
    }

    private void addLifeCycle() {
        Lifecycle lifecycle = null;
        if (mLifeCycle != null) {
            lifecycle = mLifeCycle.get();
        }
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {}


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {}


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {}


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny() {}

}

```


### 四、总结 ###

MVP只是一个对Android代码比较友好的一个架构，它能够比较对代码进行比较清晰的分层，避免Activity变得过于臃肿，但是使用它当然也不能完全保证Activity不会变得臃肿，尤其当我们在同一个Activity（Fragment）进行多功能开发时，即便只在V层进行UI操作，代码也会变得复杂，这时候就需要进行二次重构，对V层再进行模块分离，再次对V层瘦身。

其次由于是面向接口编程和结构分层，所以不可避免的是会造成生成过多的类和接口，所以这也是MVP的另一个缺点之一。但是总体来说，MVP也不失为一个合格的框架，毕竟MVP只是重构过程中的万里长征的第一步。

Github代码：[MvpDemo](https://github.com/ryanlijianchang/MvpDemo)
