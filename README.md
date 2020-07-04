![banner](https://github.com/allsoft777/Android-Youtube-Bookmark/blob/master/materials/banner_1024_500.png)

# YoutubeBookmark - Clean Architecture
이 저장소는 유튜브 영상을 북마크해서 플레이 할 수 있는 안드로이드 어플리케이션입니다. 코틀린으로 작성하였고, Clean Architecture 를 적용하였습니다.
앱 기능보다는 코드 구조와 얼마나 코틀린 스럽게 작성하였는지를 공유하고 유능한 개발자분들로부터 다른 인사이트를 얻고자 이 저장소를 만들게 되었습니다.
좀 더 효율적이고, 코틀린 스러운 코드로의 개선점이 보인다면 얼마든지 P & R 요청 부탁드립니다.


## 참고 저장소
아래 두 저장소의 소스코드를 분석후, 장점들을 조합한 새로운 Clean Architecture를 작성해보았습니다.

- [Android-CleanArchitecture-Kotlin] (https://github.com/android10/Android-CleanArchitecture-Kotlin)
- [architecture-samples] (https://github.com/android/architecture-samples)

#### 샘플앱 기능
- Google 계정을 통한 Firebase 로그인
- Youtube 영상 URL 입력을 통해 영상 정보를 가져옴 
- Youtube 영상 정보를 Local DB에 저장
- 영상 카테고리를 생성 및 수정
- 유튜브 영상을 플레이
- PIP (Picture-In-Picture)


## 라이브러리
###### Firebase
- Firestore
- Auth with google login

###### AAC(Android Architecture Component)
- Room
- Live Data
- View Model
- Data Binding

###### Kotlin
- Coroutine
- Ktx Core, Android Extensions

###### Etc
- [retrofit2] (https://square.github.io/retrofit/)
- [glide] (https://github.com/bumptech/glide)
- [android youtube player] (https://github.com/PierfrancescoSoffritti/android-youtube-player)
- [smarttablayout] (https://github.com/ogaclejapan/SmartTabLayout)
- [android-gif-drawable] (https://github.com/koral--/android-gif-drawable)

###### Unit test
- [MockK] (https://mockk.io/)
- [Robolectic] (http://robolectric.org/)
- [Kluent] (https://github.com/MarkusAmshove/Kluent)
- Espresso
- Kotlinx-coroutines (android, test)

## 앱 실행 방법
1. 저장소를 로컬에 복사합니다.
```
git clone https://github.com/allsoft777/Android-Youtube-Bookmark
```

2. [Firebase 콘솔](https://console.firebase.google.com/)에서 프로젝트를 생성 후, google-services.json을 app 패키지에 다운로드 합니다.

3. Google Cloud Console에 접속 후 youtube api를 활성화 시켜줍니다.
   유튜브 영상의 정보를 조회할때 사용하기 위함입니다.
   [가이드 페이지](https://developers.google.com/youtube/v3/getting-started?hl=ko)
   Youtube Api Key를 발급 받았으면 프로젝트 소스코드로 돌아와서 strings.xml에 youtube_api_key 의 값으로 생성합니다.
   
4. 빌드 및 실행 합니다. Android Gradle Plugin 버전은 4.0을 사용하기 때문에 Android Studio 버전을 4.0 이상으로 사용하여야 합니다.


## 패키지 레이어
![https://github.com/allsoft777/Android-Youtube-Bookmark/](https://github.com/allsoft777/Android-Youtube-Bookmark/blob/master/materials/package_layer.png)


### 레이어 설명

#### Presentation Layer
ViewModel, View 관련 클래스들이 모여있는 패키지입니다.
Activity, Fragment, View Widget 등이 위치하고, sub package는 기능별로 분류하게 됩니다.
Data 레이어와 직접적인 의존관계를 없애기 위하여 contract interface, usecase가 포함된 Domain 레이어에 의존하고 있습니다.
또한, ViewModel 을 생성하는 factory 클래스가 위치한 Injection package 또한 의존하고 있고, Entity class가 포함된 패키지에 의존하고 있습니다.

##### ViewModel 에서의 async 작업 (ViewModelScope 사용)
ViewModelScope은 앱의 각 ViewModel을 대상으로 정의됩니다. 이 범위에서 시작된 모든 코루틴은 ViewModel이 삭제되면 자동으로 취소됩니다.
코루틴은 ViewModel이 활성 상태인 경우에만 실행해야 할 작업이 있을 때 유용합니다.
예를 들어 레이아웃의 일부 데이터를 계산한다면 작업의 범위를 ViewModel로 지정하여 ViewModel을 삭제하면 리소스를 소모하지 않도록 작업이 자동으로 취소됩니다.

다음 예에서와 같이 ViewModel의 viewModelScope 속성을 통해 ViewModel의 CoroutineScope에 액세스할 수 있습니다. [Google Doc](https://developer.android.com/topic/libraries/architecture/coroutines)
```
class YoutubePlayerViewModel: ViewModel() {

    private var _entity: MutableLiveData<BookMarkEntireVO> = MutableLiveData()
    var entity: LiveData<BookMarkEntireVO> = _entity
    
    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading
    
    fun loadData(intent: Intent) {
        _dataLoading.value = true
        viewModelScope.launch {
            val dbId = intent.getIntExtra(PresentationConstants.KEY_DB_ID, -1)
            val data = bookmarkRepository.fetchBookMarkEntireType(dbId)
            _entity.value = data
            _dataLoading.value = false
        }
    }
}
```
loadData 메서드에서 viewModelScope.launch 를 통하여 내부적으로 heavy logic을 수행합니다.
이 scope 자체는 main thread에서 동작하기 때문에, repository 내부적으로 async하게 동작하게끔 기능을 구현해야 합니다.
_dataLoading은 progressbar view를 보여주기 위하여 사용하는 live data이고, _entity는 repository로부터 전달받은 결과를 저장하는 live data입니다.

 
#### Domain Layer
presentation 레이어에 repository interface 또는 UseCase를 제공하여 CRUD를 할 수 있도록 합니다.
둘 중에 한개로 통일해서 사용해도 되지만, 두가지 사용 방법을 보여주기 위하여 둘다 구현한 샘플입니다.
Coroutine을 사용하여 async 작업을 하였고, RxJava로 대체하여 사용해도 됩니다.

##### Entity 클래스의 변환 작업
Room Db의 경우 entity annotation을 사용한 entity 클래스가 있지만 이를 presentation 레이어에서 바로 사용하지 않고 caller에서 필요한 최소 정보로만 변환하여 넘겨줍니다.
프로젝트가 커질수록 화면이 늘어나고, caller의 성격에 따라서 db entity 전체를 요구할수도 있지만 일부 몇몇 field만 요구하는 경우도 있습니다.
만약 room db의 entity 클래스를 그대로 presentation 레이어에서 사용한다면 모든 필드에 접근이 가능하기 때문에 필요한 최소한의 필드만 볼 수 있도록 제한시켜야 하는 경우가 발생할 수 있습니다.
이 때에는 domain 레이어에서 적절히 mapping하여 반환하도록 구현하도록 합니다. 

```
override suspend fun fetchBookMarksSimpleType(categoryId: Int): List<BookMarkSimpleVO> =
    withContext(ioDispatcher) {
        dao.fetchBookmarks(categoryId).map { it.toSimpleVO() }
    }

override suspend fun fetchBookMarkEntireType(categoryId: Int): BookMarkEntireVO =
    withContext(ioDispatcher) {
        dao.fetchBookmark(categoryId).toEntireVO()
    }
```

```
@Parcelize
@Entity(tableName = "bookmark")
data class BookMarkEntity(
   // .....
)
   fun toEntireVO(): BookMarkEntireVO {
       return BookMarkEntireVO(
           categoryId,
           videoId,
           thumbnailUrl,
           title,
           description,
           tags,
           channelId,
           publishedAt,
           bookmarked_at,
           id
       )
   }

   fun toSimpleVO(): BookMarkSimpleVO {
       return BookMarkSimpleVO(
           categoryId,
           thumbnailUrl,
           title,
           id
       )
   }
}

```

#### Data Layer
Network, Database, Memory cache 등 CRUD 작업을 하는 레이어 입니다.


#### Entity Layer
entity class를 모아놓은 레이어입니다.
ROOM 에서 사용하는 entity 클래스는 포함하지 않습니다.


#### Injection Layer
가독성을 위하여(?) dagger와 같은 injection library를 사용하지 않았습니다.
단순히 클래스를 생성하여 반환하는 역할만을 하고 있습니다.
대상은 usecase, repository, viewmodel 클래스들입니다. 


#### Core Layer
안드로이드 프레임워크의 주요 컴포넌트들을 대상으로 기능을 확장하는 역할을 담당합니다.
Activity, Framework, View 등이 있습니다.
또한 Application 을 상속받아서 구현하는 클래스도 위치하고 있습니다.
이 클래스는 레이어상으로 가장 최 하위에 위치한 기반 클래스이기 때문에 다른 레이어와 의존관계에 있어서는 안됩니다.


## License
```
Copyright 2020 owllife.dev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```