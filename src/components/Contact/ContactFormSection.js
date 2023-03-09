import React from "react";

const ContactFormSection = () => {
  return (
    <div className="contact-form">
      <form action="#">
        <div className="row">
          <div className="col-xl-6">
            <div className="contact-from-input mb-20">
              <input type="text" placeholder="Name" />
            </div>
          </div>
          <div className="col-xl-6">
            <div className="contact-from-input mb-20">
              <input type="text" placeholder="Phone" />
            </div>
          </div>
          <div className="col-xl-6">
            <div className="contact-from-input mb-20">
              <input type="text" placeholder="Email" />
            </div>
          </div>
          <div className="col-xl-6">
            <div className="contact-select">
              <select className="mb-20">
                <option value="Subject">Assunto</option>
                <option value="Subject">Preparatório ENEM</option>
                <option value="Subject">Preparatório PAS</option>
                <option value="Subject">Aulas Particulares</option>
              </select>
            </div>
          </div>
          <div className="col-xl-12">
            <div className="contact-from-input mb-20">
              <textarea placeholder="Message" name="message"></textarea>
            </div>
          </div>
          <div className="col-xl-2 ">
            <div className="cont-btn mb-20">
              <a href="#" className="cont-btn">
                Enviar
              </a>
            </div>
          </div>
        </div>
      </form>
    </div>
  );
};

export default ContactFormSection;
