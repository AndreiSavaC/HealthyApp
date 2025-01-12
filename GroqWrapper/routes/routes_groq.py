from flask import Blueprint, jsonify, request
from groq import Groq

client = Groq()

groq_bp = Blueprint("groq", __name__)


@groq_bp.route("/reports/doctor", methods=["GET"])
def generate_doctor_report():
    data = request.get_json()
    symptoms = data.get("symptoms", [])

    if not symptoms or not isinstance(symptoms, list):
        return (
            jsonify(
                {
                    "error": "Invalid or missing 'symptoms' field. It must be a list of symptoms."
                }
            ),
            400,
        )

    system_prompt = (
        "Ești un asistent medical extrem de bine informat. Sarcina ta este să ajuți medicii "
        "analizând simptomele pacienților, generând diagnostice posibile și oferind sfaturi utile "
        "pentru îngrijirea pacienților. Fii clar, detaliat și asigură-te că sugestiile tale sunt bazate pe dovezi. "
        "Amintește-ți că răspunsurile tale vor ajuta profesioniștii medicali, așa că evită speculațiile."
    )

    user_message = f"Pacientul raportează următoarele simptome: {', '.join(symptoms)}. Te rog să oferi o analiză detaliată în limba română."

    try:
        chat_completion = client.chat.completions.create(
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_message},
            ],
            model="llama-3.3-70b-versatile",
            stream=False,
        )

        doctor_report = chat_completion.choices[0].message.content

        return jsonify({"report": doctor_report})
    except Exception as e:
        return (
            jsonify(
                {
                    "error": "An error occurred while generating the report.",
                    "details": str(e),
                }
            ),
            500,
        )


@groq_bp.route("/reports/patient", methods=["GET"])
def generate_patient_report():
    data = request.get_json()
    symptoms = data.get("symptoms", [])

    if not symptoms or not isinstance(symptoms, list):
        return (
            jsonify(
                {
                    "error": "Invalid or missing 'symptoms' field. It must be a list of symptoms."
                }
            ),
            400,
        )

    system_prompt = (
        "Ești un asistent medical bine informat. Sarcina ta este să oferi pacienților recomandări practice și utile "
        "pentru gestionarea simptomelor lor până când ajung la un doctor. Fii clar, empatic și oferă sfaturi bazate pe dovezi."
    )

    patient_message = f"Pacientul raportează următoarele simptome: {', '.join(symptoms)}. Te rog să oferi recomandări practice în limba română pentru pacient."

    try:
        patient_completion = client.chat.completions.create(
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": patient_message},
            ],
            model="llama-3.3-70b-versatile",
            stream=False,
        )
        patient_recommendations = patient_completion.choices[0].message.content

        return jsonify(
            {
                "report": patient_recommendations,
            }
        )
    except Exception as e:
        return (
            jsonify(
                {
                    "error": "An error occurred while generating the report.",
                    "details": str(e),
                }
            ),
            500,
        )