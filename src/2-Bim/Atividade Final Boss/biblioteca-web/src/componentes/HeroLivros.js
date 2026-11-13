import { useEffect, useRef } from 'react';
import * as THREE from 'three';
import { RoomEnvironment } from 'three/addons/environments/RoomEnvironment.js';

// Fileira de 32 livros 3D que sobem em onda perto do cursor.
// As capas vêm de duas folhas (2336 x 1744) com 18 capas cada; o proxy manda CORS liberado.
const LARGURA = 2336;
const ALTURA = 1744;
const PROXY =
  'https://images.higgs.ai/?default=1&output=webp&url=https%3A%2F%2Fd8j0ntlcm91z4.cloudfront.net%2Fuser_38xzZboKViGWJOttwIXH07lWA1P%2F';
const FOLHAS = [
  PROXY + 'hf_20261030_003447_7862e2d0-751c-4d7c-9c8a-e682415d908f.png&w=2336&q=90',
  PROXY + 'hf_20261030_003448_95f1eed5-2ae5-4736-a53c-f3b3f87acd3b.png&w=2336&q=90',
];

// [x, y, largura, altura, cor da lombada]; 0 a 17 da folha A, 18 a 35 da folha B
const CAPAS = [
  [6, 0, 386, 575, '#2c4f63'],
  [401, 0, 376, 575, '#195433'],
  [787, 0, 377, 575, '#c62420'],
  [1175, 0, 379, 575, '#f4f3ef'],
  [1564, 0, 377, 575, '#2d86cf'],
  [1949, 0, 383, 575, '#f7d547'],
  [6, 583, 386, 573, '#494e68'],
  [401, 583, 376, 573, '#f2ecde'],
  [787, 583, 377, 573, '#7893a3'],
  [1175, 583, 379, 573, '#0c0b0b'],
  [1564, 583, 377, 573, '#ddd0c1'],
  [1949, 583, 383, 573, '#013eb0'],
  [6, 1164, 386, 578, '#f2f1ed'],
  [401, 1164, 376, 578, '#0a291c'],
  [787, 1164, 377, 578, '#c61111'],
  [1175, 1164, 379, 578, '#7f7263'],
  [1564, 1164, 377, 578, '#ae4716'],
  [1949, 1164, 383, 578, '#021f42'],
  [3, 2, 383, 574, '#d1cd8c'],
  [394, 2, 381, 574, '#000621'],
  [784, 2, 381, 574, '#b8acd3'],
  [1172, 2, 381, 574, '#e6bd7e'],
  [1562, 2, 381, 574, '#076368'],
  [1952, 2, 382, 574, '#ad8357'],
  [3, 586, 383, 572, '#cfa94d'],
  [394, 586, 381, 572, '#497f9d'],
  [784, 586, 381, 572, '#02878a'],
  [1172, 586, 381, 572, '#170402'],
  [1562, 586, 381, 572, '#09150b'],
  [1952, 586, 382, 572, '#f57664'],
  [3, 1167, 383, 574, '#f3f3f3'],
  [394, 1167, 381, 574, '#2a2929'],
  [784, 1167, 381, 574, '#3fb1b9'],
  [1172, 1167, 381, 574, '#6b1405'],
  [1562, 1167, 381, 574, '#ceba9f'],
  [1952, 1167, 382, 574, '#1c565e'],
];

// capa de cada livro, da frente (canto inferior direito) para o fundo
const ORDEM = [
  0, 22, 5, 30, 9, 13, 26, 2, 19, 11, 33, 7, 15, 24, 4, 28, 17, 21, 1, 31, 14, 8, 25, 35, 3, 18, 10, 29, 16, 23, 6, 34,
];

const K = 70;
const C = 12.5;

// miolo do livro: creme com linhas marrons de 1px a cada 2px
function texturaPaginas(vertical) {
  const tela = document.createElement('canvas');
  tela.width = tela.height = 256;
  const ctx = tela.getContext('2d');
  ctx.fillStyle = '#efe7d6';
  ctx.fillRect(0, 0, 256, 256);
  for (let p = 0; p < 256; p += 2) {
    ctx.fillStyle = `rgba(92, 64, 38, ${0.05 + Math.random() * 0.12})`;
    if (vertical) ctx.fillRect(p, 0, 1, 256);
    else ctx.fillRect(0, p, 256, 1);
  }
  const textura = new THREE.CanvasTexture(tela);
  textura.colorSpace = THREE.SRGBColorSpace;
  return textura;
}

function HeroLivros() {
  const ref = useRef(null);

  useEffect(() => {
    const canvas = ref.current;
    let renderer;
    try {
      renderer = new THREE.WebGLRenderer({
        canvas,
        antialias: true,
        alpha: true,
      });
    } catch {
      return undefined; // sem WebGL o hero fica só com o texto
    }
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
    renderer.toneMapping = THREE.ACESFilmicToneMapping;
    renderer.toneMappingExposure = 1.05;

    const cena = new THREE.Scene();
    const pmrem = new THREE.PMREMGenerator(renderer);
    cena.environment = pmrem.fromScene(new RoomEnvironment(), 0.04).texture;
    const principal = new THREE.DirectionalLight('#fff3e4', 2.4);
    principal.position.set(-4, 6, 6);
    const contra = new THREE.DirectionalLight('#bfd2ff', 1.1);
    contra.position.set(6, 3, -4);
    cena.add(principal, contra, new THREE.AmbientLight('#ffffff', 0.12));

    const camera = new THREE.PerspectiveCamera(19.5, 1, 0.1, 100);
    camera.position.set(3.6, 2.15, 7.4);
    camera.lookAt(-1.4, 0.05, -0.6);

    let largura = 1;
    let altura = 1;
    function ajustar() {
      largura = canvas.clientWidth || 1;
      altura = canvas.clientHeight || 1;
      renderer.setSize(largura, altura, false);
      camera.fov = largura < altura || largura <= 760 ? 40 : 19.5;
      camera.aspect = largura / altura;
      camera.updateProjectionMatrix();
    }
    ajustar();
    window.addEventListener('resize', ajustar);

    const calmo = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    const toque = window.matchMedia('(pointer: coarse)').matches;
    const ponteiro = { x: 0, y: 0, ativo: false, ultimo: -Infinity };
    const aoMover = (e) => {
      ponteiro.x = e.clientX;
      ponteiro.y = e.clientY;
      ponteiro.ativo = true;
      ponteiro.ultimo = performance.now();
    };
    const aoSair = () => {
      ponteiro.ativo = false;
    };
    window.addEventListener('pointermove', aoMover);
    window.addEventListener('blur', aoSair);
    document.documentElement.addEventListener('pointerleave', aoSair);

    const geometria = new THREE.BoxGeometry(1, 1.5, 0.2);
    const corte = new THREE.MeshStandardMaterial({
      map: texturaPaginas(false),
      roughness: 0.9,
    });
    const topo = new THREE.MeshStandardMaterial({
      map: texturaPaginas(true),
      roughness: 0.9,
    });
    const livros = [];
    const ponto = new THREE.Vector3();
    let quadro = 0;
    let vivo = true;

    // ponto (0, .1, 0) do livro em pixels do canvas
    function naTela(malha) {
      ponto.set(0, 0.1, 0);
      malha.localToWorld(ponto).project(camera);
      return [((ponto.x + 1) / 2) * largura, ((1 - ponto.y) / 2) * altura];
    }

    const carregador = new THREE.TextureLoader();
    // se uma folha falhar, os livros dela ficam só com a cor da lombada
    Promise.all(FOLHAS.map((url) => carregador.loadAsync(url).catch(() => null))).then((folhas) => {
      if (!vivo) return;
      const anisotropia = renderer.capabilities.getMaxAnisotropy();

      ORDEM.forEach((n, i) => {
        const [x, y, w, h, cor] = CAPAS[n];
        const lombada = new THREE.Color(cor).multiplyScalar(0.62);
        let mapa = null;
        const folha = folhas[n < 18 ? 0 : 1];
        if (folha) {
          // corta 3px de cada lado para tirar a calha preta entre as capas
          mapa = folha.clone();
          mapa.repeat.set((w - 6) / LARGURA, (h - 6) / ALTURA);
          mapa.offset.set((x + 3) / LARGURA, 1 - (y + h - 3) / ALTURA);
          mapa.colorSpace = THREE.SRGBColorSpace;
          mapa.anisotropy = anisotropia;
          mapa.needsUpdate = true;
        }
        const malha = new THREE.Mesh(geometria, [
          corte,
          new THREE.MeshPhysicalMaterial({
            color: lombada,
            roughness: 0.55,
            clearcoat: 0.3,
          }),
          topo,
          topo,
          new THREE.MeshPhysicalMaterial({
            map: mapa,
            color: mapa ? '#ffffff' : cor,
            roughness: 0.42,
            clearcoat: 0.55,
            clearcoatRoughness: 0.35,
          }),
          new THREE.MeshStandardMaterial({ color: lombada, roughness: 0.6 }),
        ]);
        malha.position.y = calmo ? 0 : -3;
        const grupo = new THREE.Group();
        grupo.position.set(1.55 - i * 0.5, -1.12, 0.9 - i * 0.2);
        grupo.rotation.y = 0.32;
        grupo.add(malha);
        cena.add(grupo);
        // a fileira sobe do fundo para a frente
        livros.push({
          malha,
          y: malha.position.y,
          vy: 0,
          z: 0,
          vz: 0,
          entrada: calmo ? 0 : 0.25 + (32 - i) * 0.035,
        });
      });

      const relogio = new THREE.Clock();
      function desenhar() {
        quadro = requestAnimationFrame(desenhar);
        const caixa = canvas.getBoundingClientRect();
        if (caixa.bottom <= 0) return; // hero fora da tela: não gasta GPU

        const dt = Math.min(relogio.getDelta(), 0.05);
        const t = relogio.elapsedTime;
        cena.updateMatrixWorld();

        let x = ponteiro.x - caixa.left;
        let y = ponteiro.y - caixa.top;
        let ativo = ponteiro.ativo;
        // no toque, ou parado há 4s, um cursor virtual varre a fileira
        if (!calmo && (toque || performance.now() - ponteiro.ultimo > 4000)) {
          const u = (Math.sin(t * 0.45) + 1) / 2;
          const [ax, ay] = naTela(livros[29].malha);
          const [bx, by] = naTela(livros[1].malha);
          x = ax + (bx - ax) * u;
          y = ay + (by - ay) * u;
          ativo = true;
        }

        const s = Math.max(largura, altura) * 0.085;
        const passo = dt / 4;
        for (const livro of livros) {
          let subida = 0;
          if (ativo) {
            const [sx, sy] = naTela(livro.malha);
            subida = 0.78 * Math.exp(-((sx - x) ** 2 + ((sy - y) * 0.55) ** 2) / (s * s));
          }
          const alvoY = t > livro.entrada ? subida : -3;
          const alvoZ = subida * 0.35;
          // 4 subpassos por quadro: a mola fica estável em qualquer taxa de quadros
          for (let k = 0; k < 4; k++) {
            livro.vy += ((alvoY - livro.y) * K - livro.vy * C) * passo;
            livro.y += livro.vy * passo;
            livro.vz += ((alvoZ - livro.z) * K - livro.vz * C) * passo;
            livro.z += livro.vz * passo;
          }
          livro.malha.position.y = livro.y;
          livro.malha.position.z = livro.z;
          livro.malha.rotation.x = -livro.vy * 0.012;
        }
        renderer.render(cena, camera);
      }
      desenhar();
    });

    return () => {
      vivo = false;
      cancelAnimationFrame(quadro);
      window.removeEventListener('resize', ajustar);
      window.removeEventListener('pointermove', aoMover);
      window.removeEventListener('blur', aoSair);
      document.documentElement.removeEventListener('pointerleave', aoSair);
      cena.traverse((obj) => {
        if (!obj.isMesh) return;
        obj.material.forEach((m) => {
          if (m.map) m.map.dispose();
          m.dispose();
        });
      });
      geometria.dispose();
      pmrem.dispose();
      renderer.dispose();
    };
  }, []);

  return <canvas ref={ref} className="hero-palco" aria-hidden="true" />;
}

export default HeroLivros;
