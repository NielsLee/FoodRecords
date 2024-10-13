//
//  ContentView.swift
//  Fridgey-IOS
//
//  Created by 李林峰 on 2024/9/22.
//

import SwiftUI
import ComposeApp

struct ContentView: View {
    var body: some View {
        VStack {
            ComposeViewController()
        }
        .ignoresSafeArea()
    }
}

#Preview {
    ContentView()
}

struct ComposeViewController: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
