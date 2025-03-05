import Compressor from 'compressorjs'

/**
 * 压缩图片并返回压缩后的文件
 * @param file 原始图片文件
 * @param quality 压缩质量（默认 0.8）
 * @returns Promise<File | Blob>
 */
export function compressImage(file: File, quality: number = 0.8): Promise<File | Blob> {
  return new Promise((resolve, reject) => {
    new Compressor(file, {
      mimeType: file.type === 'image/png' ? 'image/jpeg' : file.type,
      quality,
      success(result: File | Blob) {
        resolve(result)
      },
      error(err) {
        reject(err)
      },
    })
  })
}
