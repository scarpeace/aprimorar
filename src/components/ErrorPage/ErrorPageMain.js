import React from "react";
import Link from "next/link";

const ErrorPageMain = () => {
  return (
    <main>
      <div className="content-error-area pt-120 pb-120">
        <div className="container">
          <div className="row justify-content-center">
            <div className="col-xl-8">
              <div className="content-error-item text-center">
                <div className="error-thumb">
                  <img
                    src="assets/img/bg/error-thumb.png"
                    alt="image not found"
                  />
                </div>
                <div className="section-title">
                  <h2 className="mb-20">
                    Oops! A página que você está procurando não está disponível
                  </h2>
                  <p>
                    Nós não conseguimos encontrar nenhum resultado para a sua
                    busca. Por favor verifique a URL informada. spelling.
                  </p>
                </div>
                <div className="error-btn">
                  <Link href="/">
                    <a className="edu-btn">Voltar para a Home</a>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default ErrorPageMain;
