


class NonEnglishFilter{

    process(lines){
        console.log(typeof (lines));
        //already did this but converts it to array to use
        return lines.map(word => word.replace(/\W/g, ''))
            .filter(word => !(word == ""));//get rid of "" that were counted
    }

}

module.exports = NonEnglishFilter;
