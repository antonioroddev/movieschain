import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        isLoggedIn: !!localStorage.getItem('token'),
        userName: localStorage.getItem('user') || 'Usuário',
    }),
    actions: {
        login(token: string, user: string) {
            localStorage.setItem('token', token)
            localStorage.setItem('user', user)
            this.isLoggedIn = true
            this.userName = user
        },
        logout() {
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            this.isLoggedIn = false
            this.userName = 'Usuário'
        },
    },
})
