const LADO_MAXIMO = 640

export function lerImagemReduzida(arquivo: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const leitor = new FileReader()
    leitor.onerror = () => reject(new Error('Não foi possível ler a imagem.'))
    leitor.onload = () => {
      const imagem = new Image()
      imagem.onerror = () => reject(new Error('Formato de imagem não suportado. Use JPG ou PNG.'))
      imagem.onload = () => {
        const escala = Math.min(1, LADO_MAXIMO / Math.max(imagem.width, imagem.height))
        const canvas = document.createElement('canvas')
        canvas.width = Math.round(imagem.width * escala)
        canvas.height = Math.round(imagem.height * escala)
        canvas.getContext('2d')?.drawImage(imagem, 0, 0, canvas.width, canvas.height)
        resolve(canvas.toDataURL('image/jpeg', 0.8))
      }
      imagem.src = String(leitor.result)
    }
    leitor.readAsDataURL(arquivo)
  })
}
