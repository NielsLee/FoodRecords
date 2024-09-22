//
//  ContentView.swift
//  Fridgey-IOS
//
//  Created by 李林峰 on 2024/9/22.
//

import SwiftUI
import ComposeApp

struct ContentView: View {
    var count = ComposeApp.ScreenParams(androidContext:nil).getListColumnCount()
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hello, world!")
            Text(String(count))
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
