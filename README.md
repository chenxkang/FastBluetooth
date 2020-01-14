# FastBluetooth
一个快速连接蓝牙打印机的工具。

# 使用方法

## build.gradle

### Step 1. Add the JitPack repository to your build file

#### Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### Step 2. Add the dependency

	dependencies {
    	 implementation 'com.github.chenxkang:FastBluetooth:v1.0.2'
    }


## 示例

#### 1、检查蓝牙开启/关闭
      BTManager.getDefault().isEnabled()；

#### 2、初始化，或注册广播、销毁广播
      BTManager.getDefault().init();
      
      BTManager.getDefault().init().register(this, mReceiver,
                BluetoothAdapter.ACTION_STATE_CHANGED);
                
      BTManager.getDefault().unregister(this, mReceiver);
      
#### 3、通过蓝牙地址连接打印机
      BTManager.getDefault().connect(address, new OnConnectListener() {
            @Override
            public void onPreConnect() {
                // 连接之前
            }

            @Override
            public void onPostConnect() {
                // 连接成功
            }

            @Override
            public void onError(String error) {
                // 连接失败
            }
        });
