export default interface SystemInfo {
  cpu: {
    cpuNum: number
    total: number
    sys: number
    used: number
    wait: number
    free: number
  }
  mem: {
    total: number
    used: number
    free: number
    usage: number
  }
  jvm: {
    total: number
    max: number
    free: number
    version: string
    home: string
    name: string
    runTime: string
    inputArgs: string[]
    startTime: string
    usage: number
    used: number
  }
  sys: {
    computerName: string
    computerIp: string
    userDir: string
    osName: string
    osArch: string
  }
  sysFiles: {
    dirName: string
    sysTypeName: string
    typeName: string
    total: string
    free: string
    used: string
    usage: number
  }[]
}
