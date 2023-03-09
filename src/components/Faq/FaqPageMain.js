import React from 'react';
import BreadcrumbTwo from '../Common/BreadcrumbTwo';
import FaqTopic from './FaqTopic';

const FaqPageMain = () => {
    return (
        <main>
            <BreadcrumbTwo breadcrumbTitleTwo={<>Welcome to help and support center! <br/>How may we help you?</>} breadcrumbSubTitleTwo="Help FAQ" />
            <FaqTopic />
        </main>
    );
};

export default FaqPageMain;