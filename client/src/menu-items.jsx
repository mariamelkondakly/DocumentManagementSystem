const menuItems = {
  items: [
    {
      id: 'navigation',
      title: 'Navigation',
      type: 'group',
      icon: 'icon-navigation',
      children: [
        {
          id: 'dashboard',
          title: 'Dashboard',
          type: 'item',
          icon: 'feather icon-home',
          url: '/Home'
        }
      ]
    },
    {
      id: 'workspaces',
      title: 'Workspaces',
      type: 'group',
      icon: 'icon-navigation',
      children: [
        {
          id: 'viewWorkspaces',
          title: 'View workspaces',
          type: 'item',
          icon: 'feather icon-folder',
          url: '/workspaces'
        }
      ]
    }
  ]
};

export default menuItems;
