import {
  setStore,
  getStore,
  removeStore
} from '@/util/store'
import website from '@/config/website'

const common = {

  state: {
    language: getStore({name: 'language'}) || 'zh',
    isCollapse: false,
    isFullScren: false,
    isMenu: true,
    isShade: false,
    screen: -1,
    isLock: getStore({name: 'isLock'}) || false,
    showLanguage:false,
    showTag: true,
    showDebug: false,
    showCollapse: true,
    showSearch: false,
    showLock: false,
    showFullScren: false,
    showTheme: false,
    showMenu: false,
    showColor: false,
    colorName: getStore({name: 'colorName'}) || '#0e1e37',
    themeName: getStore({name: 'themeName'}) || 'theme-white',
    lockPasswd: getStore({name: 'lockPasswd'}) || '',
    website: website,
  },
  mutations: {
    SET_LANGUAGE: (state, language) => {
      state.language = language
      setStore({
        name: 'language',
        content: state.language
      })
    },
    SET_SHOW_Language:(state,showLanguage)=>{
      state.language =showLanguage
      setStore({
        name: 'showLanguage',
        content: state.showLanguage
      })
    },
    SET_SHADE: (state, active) => {
      state.isShade = active;
    },
    SET_COLLAPSE: (state) => {
      state.isCollapse = !state.isCollapse;
    },
    SET_FULLSCREN: (state) => {
      state.isFullScren = !state.isFullScren;
    },
    SET_IS_MENU: (state, menu) => {
      state.isMenu = menu;
    },
    SET_LOCK: (state) => {
      state.isLock = true;
      setStore({
        name: 'isLock',
        content: state.isLock,
        type: 'session'
      })
    },
    SET_SCREEN: (state, screen) => {
      state.screen = screen;
    },
    SET_COLOR_NAME: (state, colorName) => {
      state.colorName = colorName;
      setStore({
        name: 'colorName',
        content: state.colorName,
      })
    },
    SET_THEME_NAME: (state, themeName) => {
      state.themeName = themeName;
      setStore({
        name: 'themeName',
        content: state.themeName,
      })
    },
    SET_LOCK_PASSWD: (state, lockPasswd) => {
      state.lockPasswd = lockPasswd;
      setStore({
        name: 'lockPasswd',
        content: state.lockPasswd,
        type: 'session'
      })
    },
    CLEAR_LOCK: (state) => {
      state.isLock = false;
      state.lockPasswd = '';
      removeStore({
        name: 'lockPasswd',
        type: 'session'
      });
      removeStore({
        name: 'isLock',
        type: 'session'
      });
    },
  }
}
export default common
