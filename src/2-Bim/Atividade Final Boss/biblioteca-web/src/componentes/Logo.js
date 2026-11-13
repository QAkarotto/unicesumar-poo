// Selo UL (public/img/logo.png): o tamanho vem do CSS de quem usa
function Logo({ className }) {
  return <img className={'logo ' + (className || '')} src={process.env.PUBLIC_URL + '/img/logo.png'} alt="" />;
}

export default Logo;
