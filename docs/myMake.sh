#!/usr/bin/env bash

md_list=`find source -iname '*.md'`

for md_item in ${md_list}
do
    echo "============== process" ${md_item}  "=============="

    target_file=$(echo ${md_item}| cut -d'.' -f 1 )
    target_file_dir=$(dirname ${target_file} )
    target_file_name=$(basename ${target_file})
    target_file_name=${target_file_dir}/src/${target_file_name}

    target_file=${target_file_name}".rst"
    if [ -f ${target_file} ]
    then
        echo rm ${target_file}
        rm ${target_file}
    fi
    echo pandoc --from=markdown --to=rst --output=${target_file} ${md_item}
    pandoc -V lang=zh_CN --from=markdown_github --to=rst --output=${target_file} ${md_item}
done

make html

