import SwiftUI
import Shared

@main
struct iOSApp: App {
    init(){
        InitKoin_iosKt.iOSInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
