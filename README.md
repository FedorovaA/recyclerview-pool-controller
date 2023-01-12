# RecyclerView-Pool-Controller

The RecyclerView-Pool-Controller is a small extension for working with viewholders. 
This module provides storage of previously created viewholders in the stack for further reuse.

Import this extension as a module into your project and connect it to the main project.

```groovy
implementation "com.github.FedorovaA:recyclerview-pool-controller:1.0"
```

## How to use
The module has two options for working with viewholders:
1. Simple creation of viewholders in the background thread.  
2. Inflating view using AsyncLayoutInflater, creating viewholders in the background and further transferring the created view in constructor.

In the main activity of the application take the following steps:
```kotlin
class Activity : AppCompatActivity() {

	...

	// Create a delegate variable that will pass to the module 
	// which viewholders to use for storage in the stack
	private val createVhDelegate = object : GlobalRecycledViewPoolController.CreateViewHolderDelegate {
		// simple creation
		override fun createViewHolderInBackground(viewType: Int): RecyclerView.ViewHolder? {
            ...
        }
        // with AsyncLAyoutInflater
        override fun createViewHolderWithAsyncInflater(viewType: Int, view: View): RecyclerView.ViewHolder? {
        	...
        }
	}

	// Initialize the controller
	override fun onCreate(savedInstanceState: Bundle?) {

		...

		// simple creation
        GlobalRecycledViewPoolController.initialize(List<GlobalRecycledViewPoolController.ViewHolderCacheParams>, BackgroundViewHolderInitializer(), createVhDelegate)
		// or with AsyncLAyoutInflater
        GlobalRecycledViewPoolController.initialize(List<GlobalRecycledViewPoolController.ViewHolderCacheParams>, AsyncViewHolderInitializer(this), createVhDelegate)
	}

}
```

In the place where you set the adapter, add the global pool in RecyclerView
```kotlin
	recyclerView.setRecycledViewPool(GlobalRecycledViewPool())
```

In the onCreateViewHolder method of the adapter.
```kotlin
class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	...

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		return GlobalRecycledViewPoolController.getViewHolderForType(viewType)
	}
}
```

## Restrictions
1. All the viewTypes viewholder`s that you delegate to the module during initialization must be different.  
2. All layout parameters of the main container the viewholder (width, height and etc.) must be set during initialization. 
This is necessary since the parent for viewholders is null.
