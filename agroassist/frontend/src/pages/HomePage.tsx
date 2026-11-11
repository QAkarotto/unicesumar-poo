import { Link } from 'react-router-dom'

export function HomePage() {
  return (
    <div className="landing">
      <section className="landing-hero">
        <div className="landing-copy">
          <span className="landing-kicker">TECNOLOGIA A SERVIÇO DO CAMPO</span>
          <h1>Assistência técnica mais perto de quem produz.</h1>
          <p>
            Uma forma simples de solicitar atendimento para sua lavoura, acompanhar cada etapa e avaliar o
            serviço realizado.
          </p>
          <div className="landing-actions">
            <Link className="btn landing-cta" to="/produtor/entrar">
              Começar como produtor <span aria-hidden="true">→</span>
            </Link>
            <Link className="btn landing-outline" to="/tecnico">
              Área do técnico
            </Link>
          </div>
          <span className="landing-disclaimer">
            Protótipo acadêmico demonstrativo · usuários e atendimentos fictícios
          </span>
        </div>
        <div
          className="landing-visual"
          role="img"
          aria-label="Ilustração de uma paisagem agrícola com lavouras em diferentes tons de verde"
        >
          <div className="landing-sun" />
          <div className="hill hill-one" />
          <div className="hill hill-two" />
          <div className="field-line line-one" />
          <div className="field-line line-two" />
          <div className="field-line line-three" />
          <div className="visual-label">
            🌱 AgroAssist
            <br />
            <small>Do campo ao atendimento, tudo conectado.</small>
          </div>
        </div>
      </section>
      <section className="landing-benefits" aria-label="Como funciona">
        <div>
          <strong>01 · Solicite</strong>
          <p>Registre a cultura, o problema e fotos da lavoura.</p>
        </div>
        <div>
          <strong>02 · Acompanhe</strong>
          <p>Consulte o histórico e o andamento por protocolo.</p>
        </div>
        <div>
          <strong>03 · Avalie</strong>
          <p>Receba o retorno técnico e avalie o serviço.</p>
        </div>
      </section>
      <section className="landing-footer">
        <div>
          <span className="landing-kicker">PARA CADA NECESSIDADE, UM ACESSO</span>
          <h2>Escolha como deseja explorar</h2>
        </div>
        <div className="landing-links">
          <Link to="/produtor/entrar">Produtor rural →</Link>
          <Link to="/tecnico">Profissional técnico →</Link>
          <Link to="/admin">Coordenação →</Link>
          <Link to="/modelo">Modelo e regras da AEP →</Link>
        </div>
      </section>
    </div>
  )
}
