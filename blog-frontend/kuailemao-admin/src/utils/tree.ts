/**
 * 构建树形数据
 * @param data 原始数据
 */
export function buildTree(data: any[]) {
  const tree = data.filter(item => item.parentId === null)
  tree.forEach((root) => {
    root.key = root.id
    const children = buildChildren(root, data)
    if (children.length > 0)
      root.children = children
  })
  return tree
}

/**
 * 构建子节点
 * @param parent 父节点
 * @param data 原始数据
 */
function buildChildren(parent: any, data: any[]) {
  const children = data.filter(item => item.parentId === parent.id)
  children.forEach((child) => {
    child.key = child.id
    const grandChildren = buildChildren(child, data)
    if (grandChildren.length > 0)
      child.children = grandChildren
  })
  return children
}
