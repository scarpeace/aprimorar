import React from "react";

const ContactMap = () => {
  return (
    <div className="google-map-area contact-map pt-100 mb-30">
      <iframe
        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3838.751500643098!2d-47.92016338611169!3d-15.817051789037173!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x935a3aebdb0ee05d%3A0xc098b6d3173152a3!2sClube%20dos%20Previdenci%C3%A1rios!5e0!3m2!1sen!2sbr!4v1655504195393!5m2!1sen!2sbr"
        loading="lazy"
      ></iframe>
    </div>
  );
};

export default ContactMap;
