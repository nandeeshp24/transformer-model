#!/usr/bin/env bash

function train_model() {
    python train.py \
    --model_name_or_path ./model/qald2 \
    --do_train \
    --train_file "$1" \
    --output_dir "$2" \
    --overwrite_output_dir \
    --per_device_train_batch_size=10 \
    --predict_with_generate
}

# first parameter is the path to training file and second parameter is the path to save the trained model
train_model ./../QA_module/qald1_train.json ./model/qald1
